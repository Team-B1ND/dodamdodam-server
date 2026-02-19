package com.b1nd.dodamdodam.club.application.club.data.response

import com.b1nd.dodamdodam.club.domain.club.entity.ClubTimeEntity

data class ClubTimeResponse(
    val createStart: String,
    val createEnd: String,
    val applicantStart: String,
    val applicantEnd: String,
) {
    companion object {
        fun fromEntities(createTime: ClubTimeEntity, applicantTime: ClubTimeEntity): ClubTimeResponse =
            ClubTimeResponse(
                createStart = createTime.start.toString(),
                createEnd = createTime.end.toString(),
                applicantStart = applicantTime.start.toString(),
                applicantEnd = applicantTime.end.toString(),
            )
    }
}
