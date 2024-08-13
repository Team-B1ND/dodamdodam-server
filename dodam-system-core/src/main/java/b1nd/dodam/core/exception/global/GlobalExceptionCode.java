package b1nd.dodam.core.exception.global;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {

    RESOURCE_NOT_FOUND(404, "존재하지 않는 데이터"),
    METHOD_NOT_SUPPORTED(400, "잘못된 메서드"),
    MEDIA_TYPE_NOT_SUPPORTED(400, "잘못된 미디어 타입"),
    MEDIA_TYPE_MISS_MATCHED(400, "잘못된 미디어 값"),
    PARAMETER_NOT_FOUND(400, "잘못된 파라미터"),
    PARAMETER_NOT_VALID(400, "잘못된 파라미터"),
    INVALID_PERMISSION(400, "유효하지 않은 권한"),
    INVALID_TOKEN(401, "유효하지 않은 토큰"),
    INVALID_ROLE(403, "유효하지 않은 권한"),
    TOKEN_NOT_PROVIDED(400, "잘못된 토큰"),
    TOKEN_EXPIRED(401, "만료된 토큰"),
    ENDPOINT_NOT_FOUND(400, "존재하지 않는 엔드포인트"),
    INTERNAL_SERVER(500, "서버 오류");

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
