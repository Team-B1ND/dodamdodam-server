package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;

import java.io.Serializable;

public record StudentRes(
        int id,
        String name,
        Integer grade,
        Integer room,
        Integer number) implements Serializable {
    public static StudentRes of(Student student) {
        if(student == null) {
            return null;
        }
        return new StudentRes(
                student.getId(),
                student.getMember().getName(),
                student.getGrade(), student.getRoom(),
                student.getNumber()
        );
    }
}
