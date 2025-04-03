package b1nd.dodam.sync.rdsredis.model;

import b1nd.dodam.domain.rds.member.entity.Student;

public record StudentInfoSyncModel(
            int id,
            String name,
            Integer grade,
            Integer room,
            Integer number,
            String code
) {
    public static StudentInfoSyncModel of(Student student) {
        if(student == null) {
            return null;
        }
        return new StudentInfoSyncModel(
                student.getId(),
                student.getMember().getName(),
                student.getGrade(), student.getRoom(),
                student.getNumber(),
                student.getCode()
        );
    }
}