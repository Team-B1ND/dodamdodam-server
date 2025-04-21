package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Student;

import java.util.List;

public record StudentWithNightStudyBanRes(
    int id,
    String name,
    Integer grade,
    Integer room,
    Integer number,
    String phone,
    String profileImage,
    boolean isBanned
) {

    public static List<StudentWithNightStudyBanRes> of(List<Student> students, List<Integer> banIds) {
        return students.stream()
            .map(student -> StudentWithNightStudyBanRes.of(student, banIds.contains(student.getId())))
            .toList();
    }

    public static StudentWithNightStudyBanRes of(Student student, boolean isBanned) {
        return new StudentWithNightStudyBanRes(
                student.getId(),
                student.getMember().getName(),
                student.getGrade(),
                student.getRoom(),
                student.getNumber(),
                student.getMember().getPhone(),
                student.getMember().getProfileImage(),
                isBanned
        );
    }
}
