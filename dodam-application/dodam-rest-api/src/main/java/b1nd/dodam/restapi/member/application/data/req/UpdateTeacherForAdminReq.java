package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.restapi.support.validation.Phone;

public record UpdateTeacherForAdminReq(@Phone String tel, String position) {
}
