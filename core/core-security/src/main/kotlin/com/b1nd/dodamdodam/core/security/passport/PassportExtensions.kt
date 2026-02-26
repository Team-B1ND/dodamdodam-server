package com.b1nd.dodamdodam.core.security.passport

import com.b1nd.dodamdodam.core.security.exception.PassportInvalidException
import java.util.UUID

fun Passport.requireUserId(): UUID = userId ?: throw PassportInvalidException()
fun Passport.requireUsername(): String = username ?: throw PassportInvalidException()