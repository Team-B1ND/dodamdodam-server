package com.b1nd.dodamdodam.auth.infrastructure.security.configuration

import com.b1nd.dodamdodam.auth.infrastructure.security.jwt.JwtSigner
import com.b1nd.dodamdodam.auth.infrastructure.security.jwt.properties.JwtProperties
import com.b1nd.dodamdodam.core.security.filter.PassportFilter
import com.b1nd.dodamdodam.core.security.jwt.JwtVerifier
import com.b1nd.dodamdodam.core.security.jwt.util.KeyLoader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val properties: JwtProperties,
    private val passportFilter: PassportFilter
) {
    @Bean
    fun jwtSigner(): JwtSigner = JwtSigner(
        privateKey = KeyLoader.loadPrivateKey(properties.privateKey),
        issuer = properties.issuer,
        accessExpireSeconds = properties.accessExpireSeconds,
        refreshExpireSeconds = properties.refreshExpireSeconds
    )

    @Bean
    fun jwtVerifier(): JwtVerifier = JwtVerifier(
        publicKey = KeyLoader.loadPublicKey(properties.publicKey),
        issuer = properties.issuer
    )

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .addFilterBefore(passportFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/passport", "/health", "/login").permitAll()
                auth.anyRequest().authenticated()
            }
        return http.build()
    }
}