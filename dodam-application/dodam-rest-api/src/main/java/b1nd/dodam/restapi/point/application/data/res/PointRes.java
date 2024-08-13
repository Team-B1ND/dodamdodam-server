package b1nd.dodam.restapi.point.application.data.res;

import b1nd.dodam.domain.rds.point.entity.Point;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;
import b1nd.dodam.restapi.member.application.data.res.TeacherRes;

import java.time.LocalDate;

public record PointRes(int id, PointReasonRes reason, StudentRes student, TeacherRes teacher, LocalDate issueAt) {
    public static PointRes of(Point point) {
        return new PointRes(
                point.getId(),
                PointReasonRes.of(point.getReason()),
                StudentRes.of(point.getStudent()),
                TeacherRes.of(point.getTeacher()),
                point.getIssueAt()
        );
    }
}
