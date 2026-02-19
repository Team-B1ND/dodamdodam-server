package com.b1nd.dodamdodam.auth.domain.principal.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class UserNotFoundException : BasicException(PrincipalExceptionStatusCode.USER_NOT_FOUND)
class PasswordIncorrectException : BasicException(PrincipalExceptionStatusCode.PASSWORD_INCORRECT)