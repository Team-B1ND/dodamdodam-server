package b1nd.dodam.restapi.member.application.data.req;

import jakarta.validation.constraints.NotEmpty;

public record AuthCodeReq(@NotEmpty String phone) {
}
