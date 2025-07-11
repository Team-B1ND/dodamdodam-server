package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.restapi.support.validation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record JoinTeacherReq(@NotEmpty String id, @NotEmpty String pw, @NotEmpty String name, @NotEmpty @Email String email,
                             @NotEmpty @Phone String phone, @NotEmpty @Phone String tel, @NotEmpty String position) {
    public Teacher mapToTeacher(Member member) {
        return Teacher.builder()
                .member(member)
                .tel(tel)
                .position(position)
                .build();
    }

    public Member mapToMember(String encodedPw) {
        return Member.builder()
                .id(id)
                .pw(encodedPw)
                .email(email)
                .name(name)
                .role(MemberRole.TEACHER)
                .phone(phone)
                .isAlarm(true)
                .status(ActiveStatus.PENDING)
                .build();
    }
}
