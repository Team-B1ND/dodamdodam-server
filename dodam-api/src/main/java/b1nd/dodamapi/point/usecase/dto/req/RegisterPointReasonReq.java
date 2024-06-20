package b1nd.dodamapi.point.usecase.dto.req;

import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.enums.ScoreType;
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
