package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.restapi.support.validation.Phone;
import jakarta.validation.constraints.Email;

public record UpdateMemberInfoReq(
        @Email(message = "유효하지 않은 이메일 형식")
        String email,

        String name,

        @Phone
        String phone,

        String profileImage
) {}
