package com.b1nd.dodamdodam.core.common.exception

open class BasicException(
    val exceptionCode: ExceptionCode
): RuntimeException(exceptionCode.message) {
}