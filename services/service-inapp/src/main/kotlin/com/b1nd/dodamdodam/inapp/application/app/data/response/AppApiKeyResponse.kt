package com.b1nd.dodamdodam.inapp.application.app.data.response

import java.time.LocalDateTime

data class AppApiKeyResponse(
    val apiKey: String,
    val expiredAt: LocalDateTime,
)