package com.b1nd.dodamdodam.core.security.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class AccessDeniedException(): BasicException(SecurityExceptionCode.ACCESS_DENIED)

class AuthenticationRequiredException(): BasicException(SecurityExceptionCode.AUTHENTICATION_REQUIRED)

class UserDisabledException(): BasicException(SecurityExceptionCode.USER_DISABLED)