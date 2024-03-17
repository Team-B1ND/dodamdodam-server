package b1nd.dodamcore.member.domain.vo;

import b1nd.dodamcore.member.domain.entity.Student;

public record StudentRes(
        int id,
        String name,
        Integer grade,
        Integer room,
        Integer number) {
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