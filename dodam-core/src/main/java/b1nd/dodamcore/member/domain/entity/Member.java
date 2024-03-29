package b1nd.dodamcore.member.domain.entity;

import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.common.util.ModifyUtil;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import b1nd.dodamcore.member.domain.exception.DeactivateMemberException;
import b1nd.dodamcore.member.domain.exception.WrongPasswordException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

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

    public void updatePw(String pw, PasswordEncoder passwordEncoder) {
        this.pw = passwordEncoder.encode(pw);
    }

    public void updateStatus(AuthStatus status) {
        this.status = status;
    }

    public void updateInfo(String name, String email, String phone, String profileImage) {
        this.name = ModifyUtil.modifyIfNotNull(name, this.name);
        this.email = ModifyUtil.modifyIfNotNull(email, this.email);
        this.phone = ModifyUtil.modifyIfNotNull(phone, this.phone);
        this.profileImage = ModifyUtil.modifyIfNotNull(profileImage, this.profileImage);
    }

    public boolean isActive() {
        return AuthStatus.ACTIVE == status;
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