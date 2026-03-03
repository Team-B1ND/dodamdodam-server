package com.b1nd.dodamdodam.user.domain.teacher.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class TeacherExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "선생님 정보를 찾을 수 없어요."),
    ;
}