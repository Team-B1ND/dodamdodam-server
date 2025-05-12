package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;

public record StudentWithNightStudyProjectRes(
        int id,
        String name,
        Integer grade,
        Integer room,
        Integer number,
        String projectName,
        NightStudyProjectRoom projectRoom
) {
    public static StudentWithNightStudyProjectRes of(Student student, NightStudyProject project) {
        return new StudentWithNightStudyProjectRes(
                student.getId(),
                student.getMember().getName(),
                student.getGrade(),
                student.getRoom(),
                student.getNumber(),
                project.getName(),
                project.getRoom()
        );
    }
}
