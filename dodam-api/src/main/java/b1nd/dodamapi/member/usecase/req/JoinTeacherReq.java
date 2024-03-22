package b1nd.dodamapi.member.usecase.req;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record JoinTeacherReq(@NotEmpty String id, @NotEmpty String pw, @NotEmpty String name, @NotEmpty @Email String email,
                             @NotEmpty String phone, @NotEmpty String tel, @NotEmpty String position) {
    public Teacher mapToTeacher(String encodedPw) {
        return Teacher.builder()
                .member(mapToMember(encodedPw))
                .tel(tel)
                .position(position)
                .build();
    }

    private Member mapToMember(String encodedPw) {
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
}
