package b1nd.dodam.restapi.auth.application.data.req;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(@NotBlank String id, @NotBlank String pw) {}