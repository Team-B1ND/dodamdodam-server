package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository

import java.time.LocalDate
import java.util.UUID

interface OutSleepingRepositoryCustom {
    fun existsOverlapping(studentId: UUID, startAt: LocalDate, endAt: LocalDate): Boolean
}
