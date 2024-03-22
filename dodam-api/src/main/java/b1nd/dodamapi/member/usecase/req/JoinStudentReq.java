package b1nd.dodamapi.member.usecase.req;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record JoinStudentReq(@NotEmpty String id, @NotEmpty String pw, @NotEmpty String name, @NotEmpty @Email String email,
                             @NotEmpty String phone, @NotNull int grade, @NotNull int room, @NotNull int number) {
    public Student mapToStudent(String encodedPw) {
        return Student.builder()
                .member(mapToMember(encodedPw))
                .grade(grade)
                .room(room)
                .number(number)
                .build();
    }

    private Member mapToMember(String encodedPw) {
        return Member.builder()
                .id(id)
                .pw(encodedPw)
                .email(email)
                .name(name)
                .role(MemberRole.STUDENT)
                .phone(phone)
                .status(AuthStatus.DEACTIVATE)
                .build();
    }
}
