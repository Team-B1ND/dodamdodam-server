package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClubExceptionCode implements ExceptionCode {
    CLUB_NAME_DUPLICATE(400, "이름이 중복됩니다."),
    CLUB_NOT_FOUND_EXCEPTION(404, "동아리를 찾을 수 없습니다."),
    ALREADY_IN_THE_CLUB(400, "자신은 동아리에 초대할 수 없습니다."),
    ALREADY_USER_JOIN_CREATIVE_CLUB(400, "이미 창체동아리에 가입한 유저는 창체동아리에 초대할 수 없습니다."),
    CLUB_PERMISSION_DENIED(403, "동아리 권한이 부족합니다."),
    ALREADY_CLUB_OWNER(409, "이미 다른 창체 동아리의 부장입니다"),
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
