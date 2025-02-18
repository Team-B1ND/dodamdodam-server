package b1nd.dodam.restapi.club.application.data.req;

import jakarta.validation.constraints.NotBlank;

public record UpdateClubInfoReq(
        @NotBlank String name,
        @NotBlank String shortDescription,
        @NotBlank String description,
        @NotBlank String subject
) {}