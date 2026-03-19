package com.b1nd.dodamdodam.neis.application.schedule.data

import com.b1nd.dodamdodam.neis.application.schedule.data.response.ScheduleResponse
import com.b1nd.dodamdodam.neis.domain.schedule.entity.ScheduleEntity

fun ScheduleEntity.toResponse() = ScheduleResponse(
    date = date,
    grade = grade,
    room = room,
    period = period,
    subject = subject,
    teacher = teacher,
)
