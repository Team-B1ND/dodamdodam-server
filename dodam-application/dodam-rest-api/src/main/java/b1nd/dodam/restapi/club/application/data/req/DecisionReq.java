package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import jakarta.validation.constraints.NotNull;

public record DecisionReq(
        @NotNull Long id,
        @NotNull ClubStatus status
) { }
