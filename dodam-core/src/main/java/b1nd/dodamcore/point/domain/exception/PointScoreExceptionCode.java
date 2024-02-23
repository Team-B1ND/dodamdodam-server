package b1nd.dodamcore.point.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PointScoreExceptionCode implements ExceptionCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 상벌점 점수");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
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
