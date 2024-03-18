package b1nd.dodamapi.member.usecase.req;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordReq(@NotBlank String password) {}