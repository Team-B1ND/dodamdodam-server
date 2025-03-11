package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateClubReq(
    @NotEmpty List<Long> clubIds,
    @NotNull ClubStatus status,
    String reason
) {
}
