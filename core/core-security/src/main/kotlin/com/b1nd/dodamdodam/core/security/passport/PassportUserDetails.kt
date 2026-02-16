package com.b1nd.dodamdodam.core.security.passport

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PassportUserDetails(
    val  passport: Passport
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return passport.role?.map { role ->
            SimpleGrantedAuthority(role.value)
        } ?: emptyList()
    }

    override fun getPassword(): String = ""

    override fun getUsername(): String = passport.username ?: ""

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = passport.enabled
}