package com.b1nd.dodamdodam.core.kafka.event.app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppReleaseActivatedEvent(
    val appPublicId: UUID,
    val releasePublicId: UUID,
    val appName: String,
    val githubReleaseUrl: String,
    val occurredAt: LocalDateTime = LocalDateTime.now(),
)
