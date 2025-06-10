package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

import java.util.List;

public record NightStudyProjectWithStudentsRes(
    NightStudyProjectRes project,
    List<StudentRes> students
) {
    public static NightStudyProjectWithStudentsRes of(NightStudyProject project, List<Student> students) {
        return new NightStudyProjectWithStudentsRes(
            NightStudyProjectRes.of(project),
            students.stream()
                    .map(StudentRes::of)
                    .toList()
        );
    }
}
