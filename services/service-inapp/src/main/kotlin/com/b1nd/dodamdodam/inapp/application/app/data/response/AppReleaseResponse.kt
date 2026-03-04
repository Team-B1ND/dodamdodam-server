package com.b1nd.dodamdodam.inapp.application.app.data.response

import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppReleaseStatusType
import java.time.LocalDateTime
import java.util.UUID

data class AppReleaseResponse(
    val releaseId: UUID,
    val releaseUrl: String,
    val memo: String?,
    val denyResult: String?,
    val status: AppReleaseStatusType,
    val enabled: Boolean,
    val updatedUser: UUID,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
)
