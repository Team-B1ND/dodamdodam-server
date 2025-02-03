package b1nd.dodam.domain.rds.member.entity;

import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.member.exception.ActiveMemberException;
import b1nd.dodam.domain.rds.member.exception.MemberNotActiveException;
import b1nd.dodam.domain.rds.member.exception.WrongPasswordException;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    private boolean isAlarm;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String pushToken;

    @Builder
    public Member(String id, String pw, String name, String email, MemberRole role, ActiveStatus status, String profileImage, String phone, boolean isAlarm) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.profileImage = profileImage;
        this.phone = phone;
        this.isAlarm = isAlarm;
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
            this.profileImage = profileImage;
    }

    public void updateStatus(ActiveStatus status) {
        this.status = status;
    }

    public void checkIfPasswordIsCorrect(String encodedPw) {
        if(!this.pw.equals(encodedPw)) {
            throw new WrongPasswordException();
        }
    }

    public void checkIfStatusIsActive() {
        if(this.status == ActiveStatus.ACTIVE) {
            throw new ActiveMemberException();
        }
    }

    public void checkIfStatusIncorrect() {
        if(!(this.status == ActiveStatus.ACTIVE)) {
            throw new MemberNotActiveException();
        }
    }

    public void updatePushToken(String pushToken){
        if(StringUtils.isNotBlank(pushToken)){
            this.pushToken = pushToken;
        }
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

}
