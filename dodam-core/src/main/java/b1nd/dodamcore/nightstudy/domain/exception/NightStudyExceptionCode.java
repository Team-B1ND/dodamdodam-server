package b1nd.dodamcore.nightstudy.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NightStudyExceptionCode implements ExceptionCode {

    INVALID_STUDY_DURATION(HttpStatus.BAD_REQUEST, "잘못된 심야자습 기간"),
    REASON_FOR_PHONE_MISSING(HttpStatus.BAD_REQUEST, "핸드폰 사용이유 누락"),
    APPLICATION_DURATION_PASSED(HttpStatus.FORBIDDEN, "심야자습 신청 기간이 아님"),
    STUDY_DUPLICATE(HttpStatus.FORBIDDEN, "해당 날짜에 심야자습 중복"),
    NOT_APPLICANT(HttpStatus.FORBIDDEN, "신청자가 아님"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 심야자습");

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