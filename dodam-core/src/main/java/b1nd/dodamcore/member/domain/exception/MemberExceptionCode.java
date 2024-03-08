package b1nd.dodamcore.member.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {

    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호"),
    DEACTIVATE_MEMBER(HttpStatus.FORBIDDEN, "비활성화된 멤버"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 멤버"),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 학생"),
    PARENT_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 부모님 정보"),
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 선생님"),
    MEMBER_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 멤버"),
    PARENT_MAX_EXCEEDED(HttpStatus.CONFLICT, "부모님은 1명만 등록 가능");

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
