package b1nd.dodamcore.member.application.dto.req;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record TeacherJoinReq(@NotEmpty String id, @NotEmpty String pw, @NotEmpty String name, @NotEmpty @Email String email,
                             @NotEmpty String phone, @NotEmpty String tel, @NotEmpty String position) {

    public Member mapToMember(String encodedPw) {
        return Member.builder()
                .id(id)
                .pw(encodedPw)
                .email(email)
                .name(name)
                .role(MemberRole.TEACHER)
                .phone(phone)
                .status(AuthStatus.DEACTIVATE)
                .build();
    }

    public Teacher mapToTeacher(Member member) {
        return Teacher.builder()
                .member(member)
                .tel(tel)
                .position(position)
                .build();
    }
}
