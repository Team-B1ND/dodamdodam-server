package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.redis.member.model.MemberInfoRedisModel;

import java.io.Serializable;

public record StudentRes(
        int id,
        String name,
        Integer grade,
        Integer room,
        Integer number,
        String code
) implements Serializable {
    public static StudentRes of(Student student) {
        if(student == null) {
            return null;
        }
        return new StudentRes(
                student.getId(),
                student.getMember().getName(),
                student.getGrade(), student.getRoom(),
                student.getNumber(),
                student.getCode()
        );
    }

    public static StudentRes of(MemberInfoRedisModel model) {
        return (model.studentId() != null) ?
                new StudentRes(
                        model.studentId(),
                        model.name(),
                        model.studentGrade(),
                        model.studentRoom(),
                        model.studentNumber(),
                        model.studentCode()
                ) : null;
    }
}
