package b1nd.dodam.restapi.member.application.data.res;

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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime createdAt,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime modifiedAt
) implements Serializable {
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

    public static List<MemberInfoRedisModel> toRedisModel(List<MemberInfoRes> members) {
        return members.parallelStream().map(MemberInfoRes::toRedisModel).toList();
    }

    private static MemberInfoRedisModel toRedisModel(MemberInfoRes member) {
        StudentRes student = member.student;
        return new MemberInfoRedisModel(
                member.id(),
                member.name(),
                member.email(),
                String.valueOf(member.role),
                String.valueOf(member.status),
                member.profileImage,
                member.phone,
                student == null ? "99-99-99" : String.format("%02d-%02d-%02d", student.grade(), student.room(), student.number()),
                student != null ? student.id() : null,
                student != null ? student.grade() : null,
                student != null ? student.room() : null,
                student != null ? student.number() : null,
                student != null ? student.code() : null,
                member.teacher != null ? member.teacher.id() : null,
                member.teacher != null ? member.teacher.tel() : null,
                member.teacher != null ? member.teacher.position() : null,
                member.createdAt,
                member.modifiedAt
        );
    }

    public static List<MemberInfoRes> fromRedisModel(List<MemberInfoRedisModel> models) {
        return models.parallelStream()
                .map(model -> new MemberInfoRes(
                            model.id(),
                            model.name(),
                            model.email(),
                            model.role() != null ? MemberRole.valueOf(model.role()) : null,
                            model.status() != null ? ActiveStatus.valueOf(model.status()) : null,
                            model.profileImage(),
                            model.phone(),
                            StudentRes.of(model),
                            TeacherRes.of(model),
                            model.createdAt(),
                            model.modifiedAt()
                    )
                )
                .toList();
    }

}
