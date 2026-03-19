package com.b1nd.dodamdodam.neis.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "meal")
data class MealProperties(
    val neisApiKey: String,
    val eduOfficeCode: String,
    val schoolCode: String,
)