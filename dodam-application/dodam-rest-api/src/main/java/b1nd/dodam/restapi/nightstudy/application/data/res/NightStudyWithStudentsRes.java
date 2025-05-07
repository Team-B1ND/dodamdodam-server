package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

import java.util.List;

public record NightStudyWithStudentsRes(
    NightStudyProjectRes project,
    List<StudentRes> students
) {
    public static NightStudyWithStudentsRes of(NightStudyProject project, List<Student> students) {
        return new NightStudyWithStudentsRes(
            NightStudyProjectRes.of(project),
            students.stream().map(StudentRes::of).toList()
        );
    }
}
