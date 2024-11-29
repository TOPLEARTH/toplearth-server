package com.gdsc.toplearth_server.application.service.admin;

import com.gdsc.toplearth_server.application.dto.admin.AdminInfo;
import com.gdsc.toplearth_server.application.dto.admin.ReadAdminTeamDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.admin.ReadAdminTeamResponseDto;
import com.gdsc.toplearth_server.application.dto.admin.ReadLabelResponseDto;
import com.gdsc.toplearth_server.application.dto.admin.ReadReportDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.admin.ReadReportResponseDto;
import com.gdsc.toplearth_server.application.dto.admin.ReadTeamMemberResponseDto;
import com.gdsc.toplearth_server.application.dto.admin.ReadUserDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.admin.ReadUserResponseDto;
import com.gdsc.toplearth_server.application.dto.bootstrap.TrashInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingImageDetailResponseDto;
import com.gdsc.toplearth_server.core.common.PageInfoDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.core.security.JwtDto;
import com.gdsc.toplearth_server.core.util.JwtUtil;
import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import com.gdsc.toplearth_server.domain.entity.report.Report;
import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingImagesRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.report.ReportRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.MemberRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.TeamRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.admin.AdminLoginRequestDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final MemberRepositoryImpl membersRepository;
    private final UserRepositoryImpl userRepository;
    private final TeamRepositoryImpl teamsRepository;
    private final MatchingRepositoryImpl matchingRepository;
    private final ReportRepositoryImpl reportsRepository;
    private final PloggingRepositoryImpl ploggingRepository;
    private final PloggingImagesRepositoryImpl ploggingImagesRepository;
    private final JwtUtil jwtUtil;

    public JwtDto login(AdminLoginRequestDto adminLoginRequestDto) {
        User user = userRepository.findByNicknameAndSocialId(adminLoginRequestDto.loginId(),
                        adminLoginRequestDto.password())
                .orElseThrow(() -> new CustomException(ErrorCode.CONFLICT_ADMIN_ID));

        return jwtUtil.generateTokens(user.getId(), EUserRole.ADMIN);
    }

    public ReadUserDetailResponseDto getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return null;
    }

    public Map<String, Object> searchUser(int page, int size, String sort, String text) {

        Sort sort1 = Sort.by(Direction.ASC, "nickname");

        Pageable pageable = PageRequest.of(page, size, sort1);

        Page<User> usersList = userRepository.searchUserList(text, EUserRole.USER, pageable);

        Map<String, Object> result = new HashMap<>();

        result.put("selectPrompt", usersList.getContent().stream()
                .map(ReadUserResponseDto::of)
                .collect(Collectors.toList()));
        result.put("pageInfo", new PageInfoDto(usersList));

        return result;
    }

    public Map<String, Object> getReports(int page, int size, String sort) {
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageable = PageRequest.of(page, size, sort1);

        Page<Report> reports = reportsRepository.findAll(pageable);

        Map<String, Object> result = new HashMap<>();

        result.put("selectPrompt", reports.getContent().stream()
                .map(ReadReportResponseDto::of)
                .collect(Collectors.toList()));
        result.put("pageInfo", new PageInfoDto(reports));

        return result;
    }

    public ReadReportDetailResponseDto getReportDetail(Long reportId) {
        Report report = reportsRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REPORT));

        List<PloggingImageDetailResponseDto> responseDtos = report.getPlogging().getPloggingImages().stream()
                .map(PloggingImageDetailResponseDto::fromPloggingImageEntity)
                .toList();

        return ReadReportDetailResponseDto.of(report, responseDtos);
    }

    public Boolean updateReport(Long reportId) {
        Report report = reportsRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REPORT));
        report.updateExecute();

        return true;
    }

    public ReadLabelResponseDto getLabel() {
        List<Object[]> labelCounts = ploggingImagesRepository.countByELabel();
        Map<ELabel, Long> trashCountMap = new HashMap<>();

        for (Object[] row : labelCounts) {
            String labelString = (String) row[0];
            Long count = (Long) row[1];

            if (labelString == null || labelString.isBlank()) {
                labelString = String.valueOf(ELabel.UNKNOWN);
            }
            ELabel label = ELabel.valueOf(labelString.toUpperCase());
            trashCountMap.put(label, count);
        }

        List<AdminInfo> adminInfos = ploggingRepository.ploggingMonthly().stream()
                .map(adminPloggingProjection -> AdminInfo.of(adminPloggingProjection.getTrashCount(),
                        adminPloggingProjection.getDuration())
                ).toList();

        return ReadLabelResponseDto.of(adminInfos, TrashInfoResponseDto.fromTrashCountMap(trashCountMap));
    }

    public Map<String, Object> getTeams(int page, int size, String sort, String text) {
        Sort sort1 = Sort.by(Sort.Direction.ASC, "name");

        Pageable pageable = PageRequest.of(page, size, sort1);

        Page<Team> teams = teamsRepository.searchItemList(text, pageable);

        Map<String, Object> result = new HashMap<>();

        result.put("selectPrompt", teams.getContent().stream()
                .map(ReadAdminTeamResponseDto::of)
                .collect(Collectors.toList()));
        result.put("pageInfo", new PageInfoDto(teams));

        return result;
    }

    public ReadAdminTeamDetailResponseDto getTeam(Long teamId) {
        Team team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        Integer matchCnt = matchingRepository.countByTeam(team); // 유저가 속한 팀의 매치 횟수
        Integer winCnt = matchingRepository.countByTeamAndWinFlagIsTrue(team); // 유저가 속한 팀의 승리횟수

        List<Member> members = membersRepository.findByTeam(team);

        List<ReadTeamMemberResponseDto> teamMemberResponseDtos = members.stream()
                .map(ReadTeamMemberResponseDto::of)
                .toList();

        return ReadAdminTeamDetailResponseDto.of(team, matchCnt, winCnt, teamMemberResponseDtos);
    }
}
