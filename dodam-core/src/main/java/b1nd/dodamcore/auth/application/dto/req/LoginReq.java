package b1nd.dodamcore.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(@NotBlank String id, @NotBlank String pw) {}