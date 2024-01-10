package b1nd.dodamcore.member.domain.entity;

import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import b1nd.dodamcore.member.domain.exception.DeactivateMemberException;
import b1nd.dodamcore.member.domain.exception.WrongPasswordException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    private String id;

    @NotNull
    @JsonIgnore
    private String pw;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthStatus status;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;

    private String profileImage;

    @NotNull
    private String phone;

    public Member login(String pw, PasswordEncoder passwordEncoder) {
        if(!passwordEncoder.matches(pw, this.pw)) {
            throw new WrongPasswordException();
        }

        if(status.equals(AuthStatus.DEACTIVATE)) {
            throw new DeactivateMemberException();
        }

        return this;
    }

    public void updateStatus(AuthStatus status) {
        this.status = status;
    }

    public void updateInfo(String imageUrl, String email) {
        this.profileImage = imageUrl;
        this.email = email;
    }

    public void updatePw(String pw) {
        this.pw = pw;
    }

    @Builder
    public Member(String id, String pw, String name, String email, MemberRole role, AuthStatus status, String profileImage, String phone) {
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
        this.profileImage = profileImage;
        this.phone = phone;
    }

}