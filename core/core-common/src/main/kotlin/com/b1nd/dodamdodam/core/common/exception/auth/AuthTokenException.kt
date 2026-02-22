package com.b1nd.dodamdodam.core.common.exception.auth

import com.b1nd.dodamdodam.core.common.exception.BasicException

class InvalidTokenSignatureException: BasicException(AuthTokenExceptionCode.INVALID_TOKEN_SIGNATURE)
class TokenExpiredException: BasicException(AuthTokenExceptionCode.TOKEN_EXPIRED)
