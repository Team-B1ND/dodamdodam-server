package b1nd.dodam.restapi.member.application.data.req;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordReq(@NotBlank String password) {}
