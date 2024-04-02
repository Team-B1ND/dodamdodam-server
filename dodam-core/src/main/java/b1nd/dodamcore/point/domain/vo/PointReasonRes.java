package b1nd.dodamcore.point.domain.vo;

import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.enums.ScoreType;

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
