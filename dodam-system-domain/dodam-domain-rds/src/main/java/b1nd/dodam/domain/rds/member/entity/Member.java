package b1nd.dodam.domain.rds.member.entity;

import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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

    private String parentPhone;

    @Builder
    public Member(String id, String pw, String name, String email, MemberRole role, ActiveStatus status, String profileImage, String phone, String parentPhone) {
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
        this.profileImage = profileImage;
        this.phone = phone;
        this.parentPhone = parentPhone;
    }

    public void updatePw(String pw) {
        this.pw = pw;
    }

    public void updateInfo(String name, String email, String phone, String profileImage) {
        if(StringUtils.isNotBlank(name)){
            this.name = name;
        }
        if(StringUtils.isNotBlank(email)){
            this.email = email;
        }
        if(StringUtils.isNotBlank(phone)){
            this.phone = phone;
        }
        if(StringUtils.isNotBlank(profileImage)){
            this.profileImage = profileImage;
        }
    }

    public void updateInfoForAdmin(String pw, String name, String phone, String parentPhone){
        if(StringUtils.isNotBlank(pw)){
            this.phone = pw;
        }
        if(StringUtils.isNotBlank(name)){
            this.name = name;
        }
        if(StringUtils.isNotBlank(phone)){
            this.phone = phone;
        }
        if(StringUtils.isNotBlank(parentPhone)){
            this.parentPhone = parentPhone;
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
