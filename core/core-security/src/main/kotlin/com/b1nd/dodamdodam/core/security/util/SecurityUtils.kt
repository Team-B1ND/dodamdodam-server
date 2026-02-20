package com.b1nd.dodamdodam.core.security.util

import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import org.springframework.security.core.context.SecurityContextHolder

fun getCurrentUserId(): String {
    val principal = SecurityContextHolder.getContext().authentication.principal as PassportUserDetails
    return principal.passport.userId.toString()
}

fun getCurrentUsername(): String {
    val principal = SecurityContextHolder.getContext().authentication.principal as PassportUserDetails
    return principal.passport.username!!
}
