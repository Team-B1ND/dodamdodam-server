package com.b1nd.dodamdodam.neis.infrastructure.comcigan

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "schedule")
data class ComciganProperties(
    val schoolName: String,
    val maxGrade: Int = 3,
    val maxRoom: Int = 4,
)