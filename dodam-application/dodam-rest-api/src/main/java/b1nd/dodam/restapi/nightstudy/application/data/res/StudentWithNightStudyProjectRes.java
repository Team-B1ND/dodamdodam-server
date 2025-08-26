package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;

import java.util.List;

public record StudentWithNightStudyProjectRes(
        int id,
        String name,
        Integer grade,
        Integer room,
        Integer number,
        String projectName,
        NightStudyProjectType type,
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
                project.getType(),
                project.getRoom()
        );
    }

    public static List<StudentWithNightStudyProjectRes> from(List<NightStudyProjectMember> members) {
        return members.stream()
                .map(member -> StudentWithNightStudyProjectRes.of(member.getStudent(), member.getProject()))
                .toList();
    }
}