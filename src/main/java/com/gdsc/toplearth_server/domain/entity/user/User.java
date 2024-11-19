package com.gdsc.toplearth_server.domain.entity.user;

import com.gdsc.toplearth_server.domain.entity.credit.Credit;
import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.product.Order;
import com.gdsc.toplearth_server.domain.entity.report.Report;
import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.user.type.ELoginProvider;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "goal_distance", precision = 5, scale = 3)
    private BigDecimal goalDistance;

    @Column(name = "user_role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private EUserRole userRole;

    @Column(name = "login_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELoginProvider provider;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "credit")
    @ColumnDefault("0")
    private Integer credit;

    //-------------------------------------------------

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<Mission> missions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<Credit> credits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<Plogging> plogging;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<Report> reports;

    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE)
    private Member member;

    //--------------------------------------------------

    @Builder(access = AccessLevel.PRIVATE)
    public User(String email, String nickname, EUserRole userRole, ELoginProvider provider) {
        this.email = email;
        this.nickname = nickname;
        this.goalDistance = BigDecimal.ZERO;
        this.userRole = userRole;
        this.provider = provider;
        this.profileImageUrl = null;
        this.refreshToken = null;
        this.credit = 0;
    }

    public static User toUserEntity(String email, String nickname, EUserRole userRole, ELoginProvider provider) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .userRole(userRole)
                .provider(provider)
                .build();
    }

    public void updateGoal(BigDecimal goalDistance) {
        this.goalDistance = goalDistance;
    }

    public Long getTotalDistance() {
        return this.plogging.stream()
                .mapToLong(Plogging::getDistance)
                .sum();
    }
}
