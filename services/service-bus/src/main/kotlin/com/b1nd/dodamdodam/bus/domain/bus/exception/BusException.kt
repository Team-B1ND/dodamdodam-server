package com.b1nd.dodamdodam.bus.domain.bus.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class BusNotFoundException : BasicException(BusExceptionCode.BUS_NOT_FOUND)
class BusApplicantNotFoundException : BasicException(BusExceptionCode.BUS_APPLICANT_NOT_FOUND)
class BusAlreadyAppliedPositionException : BasicException(BusExceptionCode.BUS_ALREADY_APPLIED_POSITION)
class UserNotFoundException : BasicException(BusExceptionCode.USER_NOT_FOUND)
class UserServiceException : BasicException(BusExceptionCode.USER_SERVICE_ERROR)
