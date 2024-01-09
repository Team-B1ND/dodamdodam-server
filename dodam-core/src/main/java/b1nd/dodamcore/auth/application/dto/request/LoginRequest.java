package b1nd.dodamcore.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String id, @NotBlank String pw) {}