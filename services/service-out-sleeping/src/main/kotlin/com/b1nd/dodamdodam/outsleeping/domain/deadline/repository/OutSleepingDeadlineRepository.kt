package com.b1nd.dodamdodam.outsleeping.domain.deadline.repository

import com.b1nd.dodamdodam.outsleeping.domain.deadline.entity.OutSleepingDeadlineEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.DayOfWeek

interface OutSleepingDeadlineRepository : JpaRepository<OutSleepingDeadlineEntity, Long> {
    fun findByDayOfWeek(dayOfWeek: DayOfWeek): OutSleepingDeadlineEntity?
}
