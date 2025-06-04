package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {

    WRONG_PASSWORD(401, "잘못된 비밀번호"),
    MEMBER_NOT_ACTIVE(403, "활성화되지 않은 멤버"),
    ACTIVE_MEMBER(403, "활성화된 멤버"),
    MEMBER_NOT_FOUND(404, "없는 멤버"),
    STUDENT_NOT_FOUND(404, "없는 학생"),
    PARENT_NOT_FOUND(404, "없는 부모님 정보"),
    TEACHER_NOT_FOUND(404, "없는 교사"),
    BROADCAST_CLUB_MEMBER_NOT_FOUND(404, "없는 방송부원"),
    DORMITORY_MANAGE_MEMBER_NOT_FOUND(404, "없는 자치위원"),
    MEMBER_DUPLICATED(409, "이미 존재하는 멤버"),
    BROADCAST_CLUB_MEMBER_DUPLICATED(409, "이미 존재하는 방송부원"),
    DORMITORY_MANAGE_MEMBER_DUPLICATED(409, "이미 존재하는 기숙사 자치위원"),
    CODE_NOT_FOUND(404, "없는 학생코드"),
    CHILD_DUPLICATED(409, "이미 존재하는 자녀")
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
