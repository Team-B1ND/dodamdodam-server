package com.b1nd.dodamdodam.core.security.passport.holder

import com.b1nd.dodamdodam.core.security.passport.Passport
import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import org.springframework.security.core.context.SecurityContextHolder

object PassportHolder {
    fun current(): Passport =
        (SecurityContextHolder.getContext().authentication.principal as PassportUserDetails).passport
}
