package com.b1nd.dodamdodam.core.kafka.event.app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppApiKeyCreatedEvent(
    val appPublicId: UUID,
    val apiKey: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)