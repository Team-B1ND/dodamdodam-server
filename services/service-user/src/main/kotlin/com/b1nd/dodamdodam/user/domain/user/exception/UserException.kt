package com.b1nd.dodamdodam.user.domain.user.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class UserAlreadyExistsException: BasicException(UserExceptionCode.USER_ALREADY_EXISTED)