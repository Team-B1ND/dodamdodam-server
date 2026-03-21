package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import java.time.LocalDateTime
import java.util.UUID

data class MemberResponse(
    val id: UUID,
    val name: String,
    val role: String,
    val student: StudentResponse?,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
)
