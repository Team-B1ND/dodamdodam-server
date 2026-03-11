package com.b1nd.dodamdodam.core.common.exception.base

import com.b1nd.dodamdodam.core.common.exception.BasicException

class BaseUnauthorizedException: BasicException(BaseExceptionCode.UNAUTHORIZED)

class BaseForbiddenException: BasicException(BaseExceptionCode.FORBIDDEN)

class BaseInternalServerException: BasicException(BaseExceptionCode.INTERNAL_SERVER_ERROR)