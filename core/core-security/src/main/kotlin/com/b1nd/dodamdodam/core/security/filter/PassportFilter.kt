package com.b1nd.dodamdodam.core.security.filter

import com.b1nd.dodamdodam.core.security.exception.PassportExpiredException
import com.b1nd.dodamdodam.core.security.passport.Passport
import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import com.b1nd.dodamdodam.core.security.passport.crypto.PassportCompressor
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class PassportFilter(
    private val headerName: String = "X-User-Passport"
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val rawPassport = request.getHeader(headerName)

        if (rawPassport.isNullOrBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        val passport: Passport = PassportCompressor.decompress(rawPassport)

        validateExpiredAt(passport)
        createAuthenticationHolder(passport)

        return filterChain.doFilter(request, response)
    }

    private fun validateExpiredAt(passport: Passport) {
        if (passport.expiredAt < System.currentTimeMillis()) {
            throw PassportExpiredException()
        }
    }

    private fun createAuthenticationHolder(passport: Passport) {
        val userDetails = PassportUserDetails(passport)
        SecurityContextHolder.getContext().authentication = extractAuthentication(userDetails)
    }

    private fun extractAuthentication(userDetails: PassportUserDetails): UsernamePasswordAuthenticationToken =
        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
}
