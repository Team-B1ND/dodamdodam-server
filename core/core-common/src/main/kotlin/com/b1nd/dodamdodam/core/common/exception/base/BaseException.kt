package com.b1nd.dodamdodam.core.common.exception.base

import com.b1nd.dodamdodam.core.common.exception.BasicException

class BaseInternalServerException: BasicException(BaseExceptionCode.INTERNAL_SERVER_ERROR)