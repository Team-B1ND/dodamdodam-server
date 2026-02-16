package com.b1nd.dodamdodam.core.security.configuration

import com.b1nd.dodamdodam.core.security.annotation.autnetication.UserAccessAspect
import com.b1nd.dodamdodam.core.security.filter.PassportFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableAspectJAutoProxy
class CoreSecurityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun passportFilter() = PassportFilter()

    @Bean
    fun userAccessAspect() = UserAccessAspect()

}