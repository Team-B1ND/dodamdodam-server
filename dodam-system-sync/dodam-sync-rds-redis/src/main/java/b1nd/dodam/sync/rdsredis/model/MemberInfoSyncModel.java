package b1nd.dodam.sync.rdsredis.model;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.redis.member.model.MemberInfoRedisModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record MemberInfoSyncModel(
        String id,
        String name,
        String email,
        MemberRole role,
        ActiveStatus status,
        String profileImage,
        String phone,
        StudentInfoSyncModel student,
        TeacherInfoSyncModel teacher,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime createdAt,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime modifiedAt
) {

    public static MemberInfoSyncModel of(Member member, Student student, Teacher teacher) {
        return new MemberInfoSyncModel(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole(),
                member.getStatus(),
                member.getProfileImage(),
                member.getPhone(),
                StudentInfoSyncModel.of(student),
                TeacherInfoSyncModel.of(teacher),
                member.getCreatedAt(),
                member.getModifiedAt()
        );
    }

    public static MemberInfoRedisModel toRedisModel(MemberInfoSyncModel member) {
        return new MemberInfoRedisModel(
                member.id(),
                member.name(),
                member.email(),
                String.valueOf(member.role),
                String.valueOf(member.status),
                member.profileImage,
                member.phone,
                member.student != null ? member.student.id() : null,
                member.student != null ? member.student.grade() : null,
                member.student != null ? member.student.room() : null,
                member.student != null ? member.student.number() : null,
                member.student != null ? member.student.code() : null,
                member.teacher != null ? member.teacher.id() : null,
                member.teacher != null ? member.teacher.tel() : null,
                member.teacher != null ? member.teacher.position() : null,
                member.createdAt,
                member.modifiedAt
        );
    }

    public static MemberInfoRedisModel toRedisModel(Member member, Student student, Teacher teacher) {
        return new MemberInfoRedisModel(
                member.getId(),
                member.getName(),
                member.getEmail(),
                String.valueOf(member.getRole()),
                String.valueOf(member.getStatus()),
                member.getProfileImage(),
                member.getPhone(),
                student != null ? student.getId() : null,
                student != null ? student.getGrade() : null,
                student != null ? student.getRoom() : null,
                student != null ? student.getNumber() : null,
                student != null ? student.getCode() : null,
                teacher != null ? teacher.getId() : null,
                teacher != null ? teacher.getTel() : null,
                teacher != null ? teacher.getPosition() : null,
                member.getCreatedAt(),
                member.getModifiedAt()
        );
    }




}
