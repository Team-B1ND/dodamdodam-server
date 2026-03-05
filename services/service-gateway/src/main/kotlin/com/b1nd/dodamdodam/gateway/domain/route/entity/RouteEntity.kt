package com.b1nd.dodamdodam.gateway.domain.route.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("routes")
data class RouteEntity(
    @Id
    val id: Long? = null,
    val serviceId: UUID,
    val path: String,
    val targetUri: String,
    val stripPrefix: Int,
    val enabled: Boolean,
    val createdAt: LocalDateTime? = null,
    val modifiedAt: LocalDateTime? = null
)
