package b1nd.dodam.restapi.point.application.data.res;

import b1nd.dodam.domain.rds.point.entity.PointReason;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.enumeration.ScoreType;

public record PointReasonRes(int id, String reason, int score, ScoreType scoreType, PointType pointType) {
    public static PointReasonRes of(PointReason reason) {
        return new PointReasonRes(
                reason.getId(),
                reason.getReason(),
                reason.getScore(),
                reason.getScoreType(),
                reason.getPointType()
        );
    }
}
