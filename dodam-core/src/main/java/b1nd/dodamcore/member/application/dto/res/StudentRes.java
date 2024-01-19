package b1nd.dodamcore.member.application.dto.res;

import b1nd.dodamcore.member.domain.entity.Student;

public record StudentRes(
        Integer id,
        String name,
        Integer grade,
        Integer room,
        Integer number) {
    public static StudentRes of(Student student) {
        return new StudentRes(
                student.getId(),
                student.getMember().getName(),
                student.getGrade(), student.getRoom(),
                student.getNumber()
        );
    }
}