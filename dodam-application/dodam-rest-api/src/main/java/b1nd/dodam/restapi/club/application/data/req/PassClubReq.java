package b1nd.dodam.restapi.club.application.data.req;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PassClubReq(
        @NotNull List<Long> ids,
        @NotNull Long clubId
) { }
