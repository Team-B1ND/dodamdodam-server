package com.b1nd.dodamdodam.bus.application.bus.data.response

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusApplicantEntity
import com.b1nd.dodamdodam.bus.domain.bus.enumeration.BoardingType

data class BusApplicantResponse(
    val id: Long,
    val name: String,
    val seat: Int?,
    val boardingType: BoardingType
) {
    companion object {
        fun of(applicant: BusApplicantEntity?): BusApplicantResponse? {
            if (applicant == null) return null
            return BusApplicantResponse(
                id = applicant.bus.id!!,
                name = applicant.bus.name,
                seat = applicant.seat,
                boardingType = applicant.boardingType
            )
        }
    }
}
