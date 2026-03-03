package com.b1nd.dodamdodam.user.domain.student.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class StudentExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "학생정보를 찾을 수 없어요."),
}