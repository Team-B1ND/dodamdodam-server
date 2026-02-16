package com.b1nd.dodamdodam.core.security.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class PassportExpiredException(): BasicException(PassportExceptionCode.PASSPORT_EXPIRED)

class PassportNotFoundException(): BasicException(PassportExceptionCode.PASSPORT_NOT_FOUND)

class PassportInvalidException(): BasicException(PassportExceptionCode.PASSPORT_INVALID)

class PassportDecompressionException(): BasicException(PassportExceptionCode.PASSPORT_DECOMPRESSION_FAILED)

class PassportSerializationException(): BasicException(PassportExceptionCode.PASSPORT_SERIALIZATION_FAILED)