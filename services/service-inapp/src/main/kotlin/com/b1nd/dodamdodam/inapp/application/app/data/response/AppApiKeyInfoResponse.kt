package com.b1nd.dodamdodam.inapp.application.app.data.response

import java.time.LocalDateTime

data class AppApiKeyInfoResponse(
    val expiredAt: LocalDateTime,
    val isExpired: Boolean,
    val createdAt: LocalDateTime?,
)