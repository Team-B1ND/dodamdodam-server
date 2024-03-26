package b1nd.dodamcore.point.application.dto.res;

import b1nd.dodamcore.member.domain.vo.StudentRes;
import b1nd.dodamcore.member.domain.vo.TeacherRes;
import b1nd.dodamcore.point.domain.entity.Point;

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