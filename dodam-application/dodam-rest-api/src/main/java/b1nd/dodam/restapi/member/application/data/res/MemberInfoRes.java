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
