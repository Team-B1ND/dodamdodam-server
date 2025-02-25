package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;

import java.io.Serializable;

public record StudentWithImageRes(
        int id,
        String name,
        Integer grade,
        Integer room,
        Integer number,
        String profileImage
) implements Serializable {
    public static StudentWithImageRes of(Student student) {
        if(student == null) {
            return null;
        }
        return new StudentWithImageRes(
                student.getId(),
                student.getMember().getName(),
                student.getGrade(),
                student.getRoom(),
                student.getNumber(),
                student.getMember().getProfileImage()
        );
    }
}
