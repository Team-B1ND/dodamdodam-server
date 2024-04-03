package b1nd.dodamcore.wakeupsong.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum WakeupSongExceptionCode implements ExceptionCode {

    ALREADY_APPLIED(HttpStatus.LOCKED, "이미 이번주에 기상송을 신청함"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 기상송"),
    NOT_APPLICANT(HttpStatus.FORBIDDEN, "신청자가 아님"),
    URL_MALFORMED(HttpStatus.BAD_REQUEST, "잘못된 유튜브 URL 형식");

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