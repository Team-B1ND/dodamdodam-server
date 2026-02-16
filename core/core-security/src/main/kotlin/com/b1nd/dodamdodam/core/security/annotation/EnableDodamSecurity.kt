package com.b1nd.dodamdodam.core.security.annotation

import com.b1nd.dodamdodam.core.security.configuration.CoreSecurityAutoConfiguration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(CoreSecurityAutoConfiguration::class)
annotation class EnableDodamSecurity
