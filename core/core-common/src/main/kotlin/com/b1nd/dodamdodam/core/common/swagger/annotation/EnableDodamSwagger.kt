package com.b1nd.dodamdodam.core.common.swagger.annotation

import com.b1nd.dodamdodam.core.common.swagger.configuration.CoreSwaggerConfiguration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(CoreSwaggerConfiguration::class)
annotation class EnableDodamSwagger
