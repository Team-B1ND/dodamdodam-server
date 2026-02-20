package com.b1nd.dodamdodam.bus.infrastructure.security.configuration

import com.b1nd.dodamdodam.core.security.filter.PassportFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val passportFilter: PassportFilter
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .addFilterBefore(passportFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/health", "/bus/health").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/bus", "/bus/{id}", "/bus/{id}/seats").permitAll()
                auth.requestMatchers(HttpMethod.POST, "/bus").hasAnyRole("TEACHER", "ADMIN")
                auth.requestMatchers(HttpMethod.POST, "/bus/board").hasAnyRole("TEACHER", "ADMIN")
                auth.requestMatchers(HttpMethod.PUT, "/bus/{id}").hasAnyRole("TEACHER", "ADMIN")
                auth.requestMatchers(HttpMethod.DELETE, "/bus/{id}").hasAnyRole("TEACHER", "ADMIN")
                auth.anyRequest().authenticated()
            }
        return http.build()
    }
}
