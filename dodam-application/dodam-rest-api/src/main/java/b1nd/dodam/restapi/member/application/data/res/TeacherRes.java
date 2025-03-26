package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.redis.member.model.MemberInfoRedisModel;

import java.io.Serializable;

public record TeacherRes(int id, String name, String tel, String position) implements Serializable {
    public static TeacherRes of(Teacher teacher) {
        if(teacher == null) {
            return null;
        }
        return new TeacherRes(teacher.getId(), teacher.getMember().getName(), teacher.getTel(), teacher.getPosition());
    }

    public static TeacherRes of(MemberInfoRedisModel model) {
        return (model.teacherId() != null) ?
                new TeacherRes(
                        model.teacherId(),
                        model.name(),
                        model.teacherTel(),
                        model.teacherPosition()
                ) : null;
    }
}
