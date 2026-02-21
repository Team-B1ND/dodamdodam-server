package com.b1nd.dodamdodam.core.kafka.event.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserCreatedEvent(
    val publicId: UUID? = null,
    val username: String = "",
    val status: Boolean = false,
    val name: String = "",
    val phone: String? = null,
    val profileImage: String? = null,
    val role: String = "STUDENT",
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserUpdatedEvent(
    val publicId: UUID? = null,
    val username: String = "",
    val status: Boolean = false,
    val roles: Collection<String> = emptyList(),
    val name: String = "",
    val phone: String? = null,
    val profileImage: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
