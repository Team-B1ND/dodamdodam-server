package com.b1nd.dodamdodam.gateway.infrastructure.auth.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class PassportExchangeFailedException(): BasicException(AuthClientExceptionCode.PASSPORT_EXCHANGE_FAILED)
