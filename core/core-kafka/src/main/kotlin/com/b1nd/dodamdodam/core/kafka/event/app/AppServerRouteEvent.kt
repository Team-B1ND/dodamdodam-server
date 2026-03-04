package com.b1nd.dodamdodam.core.kafka.event.app

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppServerRouteEvent(
    val appId: UUID,
    val eventType: AppServerRouteEventType,
    val path: String,
    val targetUri: String,
    val stripPrefix: Int,
    val enabled: Boolean,
    val occurredAt: LocalDateTime = LocalDateTime.now(),
)

enum class AppServerRouteEventType {
    CREATED,
    UPDATED,
}
