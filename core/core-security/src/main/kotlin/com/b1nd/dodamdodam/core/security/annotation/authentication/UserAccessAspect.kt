package com.b1nd.dodamdodam.core.security.annotation.authentication

import com.b1nd.dodamdodam.core.security.exception.AccessDeniedException
import com.b1nd.dodamdodam.core.security.exception.UserDisabledException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

@Aspect
class UserAccessAspect {
    @Around("@annotation(userAccess)")
    fun around(joinPoint: ProceedingJoinPoint, userAccess: UserAccess): Any? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication

        val principalEnabled: Boolean = (authentication?.principal as? UserDetails)?.isEnabled ?: false

        if (userAccess.enabledOnly && !principalEnabled) {
            throw UserDisabledException()
        }

        val roles = userAccess.roles
        val hasRole = roles.isEmpty() || roles.any { role ->
            authentication?.authorities?.any { it.authority == role.value } ?: false
        }

        if (!hasRole) {
            throw AccessDeniedException()
        }

        return joinPoint.proceed()
    }
}