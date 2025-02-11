package b1nd.dodam.domain.redis.member.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public enum MemberInfraExceptionCode implements ExceptionCode {

    AUTH_CODE_NOT_MATCH(403, "인증코드가 일치하지 않음"),
    MEMBER_INVALID(403, "유효하지 않은 인증");


    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getExceptionName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
