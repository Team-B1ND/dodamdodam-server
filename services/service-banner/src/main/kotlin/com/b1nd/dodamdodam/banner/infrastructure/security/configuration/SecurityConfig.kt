package com.b1nd.dodamdodam.banner.infrastructure.security.configuration

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
                auth.requestMatchers("/health").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/banner/active").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/banner/{id}").permitAll()
                auth.anyRequest().authenticated()
            }
        return http.build()
    }
}
