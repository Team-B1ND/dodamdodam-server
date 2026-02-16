package com.b1nd.dodamdodam.core.security.annotation.authentication

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserAccess(
    val roles: Array<RoleType> = [],
    val enabledOnly: Boolean = true
)
