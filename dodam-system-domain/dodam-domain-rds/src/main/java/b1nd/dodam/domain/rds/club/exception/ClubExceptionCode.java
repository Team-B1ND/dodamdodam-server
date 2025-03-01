package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClubExceptionCode implements ExceptionCode {
    CLUB_NAME_DUPLICATE(400, "이름이 중복됩니다."),
    CLUB_NOT_FOUND_EXCEPTION(404, "동아리를 찾을 수 없습니다."),
    INVALID_CLUB_MEMBER_INVITATION(400, "초대할 수 없는 학생입니다"),
    CLUB_PERMISSION_DENIED(403, "동아리 권한이 부족합니다."),
    ClUB_MEMBER_NOT_FOUND(404, "찾을 수 없는 동아리 부원입니다."),
    APPLICATION_DURATION_PASSED(403, "동아리 신청 기간이 아님")
    ;

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
