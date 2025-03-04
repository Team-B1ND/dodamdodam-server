package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PassClubReq(
        @NotNull List<Long> ids,
        @NotNull Long clubId
) { }
