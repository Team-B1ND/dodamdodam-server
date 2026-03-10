package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class OutSleepingNotFoundException : BasicException(OutSleepingExceptionCode.OUT_SLEEPING_NOT_FOUND)
class OutSleepingNotOwnerException : BasicException(OutSleepingExceptionCode.OUT_SLEEPING_NOT_OWNER)
