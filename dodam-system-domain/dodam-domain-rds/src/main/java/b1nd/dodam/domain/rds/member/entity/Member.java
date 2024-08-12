package b1nd.dodam.domain.rds.member.entity;

import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
    private ActiveStatus status;

    private String profileImage;

    @NotNull
    private String phone;

    @Builder
    public Member(String id, String pw, String name, String email, MemberRole role, ActiveStatus status, String profileImage, String phone) {
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
        this.profileImage = profileImage;
        this.phone = phone;
    }

    public void updatePw(String pw) {
        this.pw = pw;
    }

    public void updateInfo(String name, String email, String phone, String profileImage) {
        if(Objects.nonNull(name)) {
            this.name = name;
        }
        if(Objects.nonNull(email)) {
            this.email = email;
        }
        if(Objects.nonNull(phone)) {
            this.phone = phone;
        }
        if(Objects.nonNull(profileImage)) {
            this.profileImage = profileImage;
        }
    }

    public void updateStatus(ActiveStatus status) {
        this.status = status;
    }

    public boolean isCorrectPw(String pw) {
        return this.pw.equals(pw);
    }

    public boolean isActive() {
        return ActiveStatus.ACTIVE == this.status;
    }

}
