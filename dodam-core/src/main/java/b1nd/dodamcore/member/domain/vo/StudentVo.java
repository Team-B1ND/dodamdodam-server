package b1nd.dodamcore.member.domain.vo;

import b1nd.dodamcore.member.domain.entity.Student;

public record StudentVo(Integer id, String name, Integer grade, Integer room, Integer number) {
    public static StudentVo of(Student student) {
        return new StudentVo(student.getId(), student.getMember().getName(), student.getGrade(), student.getRoom(), student.getNumber());
    }
}