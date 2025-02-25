package b1nd.dodam.restapi.club.application.data.req;

import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JoinClubReq(
        @NotNull Long club,
        @NotNull ClubType clubType,
        ClubPriority clubPriority,
        @NotBlank String introduction) {
}
