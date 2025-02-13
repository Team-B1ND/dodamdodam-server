package b1nd.dodam.restapi.member.application.data.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record VerifyAuthCodeReq(@NotEmpty String identifier, @NotNull int authCode, Optional<String> phone) {
}
