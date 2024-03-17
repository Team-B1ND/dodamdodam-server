package b1nd.dodamcore.member.domain.vo;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.enums.MemberRole;

import java.time.LocalDateTime;

public record MemberInfoRes(
        String id,
        String name,
        String email,
        MemberRole role,
        AuthStatus status,
        String profileImage,
        String phone,
        StudentRes student,
        TeacherRes teacher,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static MemberInfoRes of(Member member, Student student, Teacher teacher) {
        return new MemberInfoRes(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole(),
                member.getStatus(),
                member.getProfileImage(),
                member.getPhone(),
                StudentRes.of(student),
                TeacherRes.of(teacher),
                member.getCreatedAt(),
                member.getModifiedAt()
        );
    }
}