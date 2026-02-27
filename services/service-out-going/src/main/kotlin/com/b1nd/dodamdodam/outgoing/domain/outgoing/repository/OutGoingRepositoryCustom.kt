package com.b1nd.dodamdodam.outgoing.domain.outgoing.repository

import java.time.LocalDateTime
import java.util.UUID

interface OutGoingRepositoryCustom {
    fun existsOverlapping(studentId: UUID, startAt: LocalDateTime, endAt: LocalDateTime): Boolean
}
