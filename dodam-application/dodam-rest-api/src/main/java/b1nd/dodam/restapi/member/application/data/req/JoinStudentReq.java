package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.restapi.support.util.RandomCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public record JoinStudentReq(@NotEmpty String id, @NotEmpty String pw, @NotEmpty String name, @NotEmpty @Email String email,
                             @NotEmpty String phone, @NotNull int grade, @NotNull int room, @NotNull int number) {
    public Student mapToStudent(Member member) {
        return Student.builder()
                .member(member)
                .grade(grade)
                .room(room)
                .number(number)
                .code(RandomCode.UUIDRandomCode())
                .build();
    }

    public Member mapToMember(String encodedPw) {
        return Member.builder()
                .id(id)
                .pw(encodedPw)
                .email(email)
                .name(name)
                .role(MemberRole.STUDENT)
                .phone(phone)
                .isAlarm(true)
                .status(ActiveStatus.PENDING)
                .build();
    }

}
