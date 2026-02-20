package com.b1nd.dodamdodam.core.security.util

import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import org.springframework.security.core.context.SecurityContextHolder

fun getCurrentUserId(): String {
    val userDetails = SecurityContextHolder.getContext().authentication.principal as PassportUserDetails
    return userDetails.passport.userId.toString()
}
