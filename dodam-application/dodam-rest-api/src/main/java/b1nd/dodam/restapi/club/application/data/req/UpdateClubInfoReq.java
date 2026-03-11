package b1nd.dodam.restapi.club.application.data.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateClubInfoReq(
    @Size(max = 10) @NotBlank String name,
    @Size(max = 30) @NotBlank String shortDescription,
    @NotBlank String description,
    @NotBlank String image,
    @Size(max = 4) @NotBlank String subject
) {}