package b1nd.dodamcore.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record ReissueTokenReq(@NotBlank String refreshToken) {}