package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;

public record StudentWithImageRes(
        int id,
        String name,
        Integer grade,
        Integer room,
        Integer number,
        String profileImage
) {
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
