package com.b1nd.dodamdodam.neis.infrastructure.comcigan.data

import java.time.LocalDate

data class ParsedSchedule(
    val date: LocalDate,
    val grade: Int,
    val room: Int,
    val period: Int,
    val subject: String,
    val teacher: String,
)