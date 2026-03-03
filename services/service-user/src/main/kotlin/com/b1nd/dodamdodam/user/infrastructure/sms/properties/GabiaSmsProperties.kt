package com.b1nd.dodamdodam.user.infrastructure.sms.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.sms.gabia")
data class GabiaSmsProperties(
    val id: String = "",
    val apiKey: String = "",
    val refKey: String = "",
    val subject: String = "",
    val senderNumber: String = "",
    val tokenUrl: String = "https://sms.gabia.com/oauth/token",
    val smsUrl: String = "https://sms.gabia.com/api/send/sms",
    val lmsUrl: String = "https://sms.gabia.com/api/send/lms"
)
