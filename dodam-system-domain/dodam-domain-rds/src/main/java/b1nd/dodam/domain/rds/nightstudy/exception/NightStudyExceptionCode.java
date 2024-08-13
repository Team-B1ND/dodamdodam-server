package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NightStudyExceptionCode implements ExceptionCode {

    INVALID_STUDY_DURATION(400, "잘못된 심야자습 기간"),
    REASON_FOR_PHONE_MISSING(400, "핸드폰 사용이유 누락"),
    APPLICATION_DURATION_PASSED(403, "심야자습 신청 기간이 아님"),
    NOT_APPLICANT(403, "신청자가 아님"),
    NOT_FOUND(404, "없는 심야자습"),
    STUDY_DUPLICATE(409, "해당 날짜에 심야자습 중복");

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
