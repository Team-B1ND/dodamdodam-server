package com.b1nd.dodamdodam.core.security.util

import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import org.springframework.security.core.context.SecurityContextHolder

fun getCurrentUserId(): String {
    val authentication = SecurityContextHolder.getContext().authentication
    val userDetails = authentication?.principal as? PassportUserDetails
        ?: throw IllegalStateException("인증 정보를 찾을 수 없어요")
    return userDetails.passport.userId?.toString()
        ?: throw IllegalStateException("사용자 ID를 찾을 수 없어요")
}
