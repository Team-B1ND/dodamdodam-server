package com.b1nd.dodamdodam.core.common.swagger.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.swagger")
data class SwaggerProperties(
    val enabled: Boolean = false,
    val gatewayPrefix: String = "/"
)
