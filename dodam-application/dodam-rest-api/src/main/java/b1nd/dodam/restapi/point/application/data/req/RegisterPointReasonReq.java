package b1nd.dodam.restapi.point.application.data.req;

import b1nd.dodam.domain.rds.point.entity.PointReason;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.enumeration.ScoreType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterPointReasonReq(@NotNull PointType pointType, @NotBlank String reason, @NotNull ScoreType scoreType, int score) {
    public PointReason toEntity() {
        return PointReason.builder()
                .score(score)
                .scoreType(scoreType)
                .reason(reason)
                .pointType(pointType)
                .build();
    }
}
