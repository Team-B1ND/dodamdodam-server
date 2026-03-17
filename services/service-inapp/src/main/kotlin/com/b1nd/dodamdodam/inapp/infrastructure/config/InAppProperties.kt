package com.b1nd.dodamdodam.inapp.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("inapp")
data class InAppProperties(
    val s3BaseUrl: String = "",
)
