package b1nd.dodam.restapi.member.application.data.req;

import jakarta.validation.constraints.NotNull;

public record UpdateStudentInfoReq(
        @NotNull Integer grade,
        @NotNull Integer room,
        @NotNull Integer number
) {}
