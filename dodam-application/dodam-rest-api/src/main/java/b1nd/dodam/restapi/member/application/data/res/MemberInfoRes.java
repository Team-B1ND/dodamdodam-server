package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;

import java.time.LocalDateTime;

public record MemberInfoRes(
        String id,
        String name,
        String email,
        MemberRole role,
        ActiveStatus status,
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
