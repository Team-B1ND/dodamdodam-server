package com.b1nd.dodamdodam.gateway.domain.route.entity

import kotlinx.datetime.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("routes")
data class RouteEntity(
    @Id
    val id: Long,
    val serviceId: UUID,
    val path: String,
    val targetUri: String,
    val stripPrefix: Int,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)