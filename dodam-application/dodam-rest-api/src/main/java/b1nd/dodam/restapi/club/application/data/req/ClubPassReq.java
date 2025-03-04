package b1nd.dodam.restapi.club.application.data.req;

import jakarta.validation.constraints.NotNull;

public record ClubPassReq(
        @NotNull Long clubId,
        @NotNull Long clubMemberId
) {
}
