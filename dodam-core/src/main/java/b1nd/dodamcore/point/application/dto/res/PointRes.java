package b1nd.dodamcore.point.application.dto.res;

import b1nd.dodamcore.member.application.dto.res.StudentRes;
import b1nd.dodamcore.point.domain.entity.Point;

import java.time.LocalDate;

public record PointRes(int id, PointReasonRes reason, StudentRes student, LocalDate issueAt) {
    public static PointRes of(Point point) {
        return new PointRes(
                point.getId(),
                PointReasonRes.of(point.getReason()),
                StudentRes.of(point.getStudent()),
                point.getIssueAt()
        );
    }
}