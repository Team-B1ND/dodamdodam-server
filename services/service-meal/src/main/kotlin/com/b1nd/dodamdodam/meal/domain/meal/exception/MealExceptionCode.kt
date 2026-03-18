package com.b1nd.dodamdodam.meal.domain.meal.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class MealExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "급식 정보를 찾을 수 없어요."),
    NEIS_API_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "나이스 API 호출에 실패했어요."),
    ;
}