package com.gdsc.toplearth_server.domain.entity.user;

import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = "user_role", nullable = false, length = 10)
    private EUserRole userRole;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public User(String email, String nickname, EUserRole userRole) {
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
        this.profileImageUrl = null;
        this.refreshToken = null;
    }

    public static User toUserEntity(String email, String nickname, EUserRole userRole) {
        return User.builder()
            .email(email)
            .nickname(nickname)
            .userRole(userRole)
            .build();
    }
}
