package b1nd.dodam.domain.rds.wakeupsong.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WakeupSongExceptionCode implements ExceptionCode {

    ALREADY_APPLIED(423, "이미 이번주에 기상송을 신청함"),
    NOT_FOUND(404, "없는 기상송"),
    NOT_APPLICANT(403, "신청자가 아님"),
    URL_MALFORMED(400, "잘못된 유튜브 URL 형식"),
    UNSUPPORTED_TYPE(422,"지원하지 않는 유형의 비디오");

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
