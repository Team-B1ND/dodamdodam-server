package com.b1nd.dodamdodam.core.common.exception

open class DodamException(
    override val message: String,
    val code: String
) : RuntimeException(message)

class BadRequestException(message: String, code: String = "BAD_REQUEST") : DodamException(message, code)
class UnauthorizedException(message: String, code: String = "UNAUTHORIZED") : DodamException(message, code)
class ForbiddenException(message: String, code: String = "FORBIDDEN") : DodamException(message, code)
class NotFoundException(message: String, code: String = "NOT_FOUND") : DodamException(message, code)
