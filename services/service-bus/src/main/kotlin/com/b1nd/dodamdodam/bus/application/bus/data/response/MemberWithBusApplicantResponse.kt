package com.b1nd.dodamdodam.bus.application.bus.data.response

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusApplicantEntity
import com.b1nd.dodamdodam.bus.domain.bus.enumeration.BoardingType

data class MemberWithBusApplicantResponse(
    val boardingType: BoardingType,
    val seat: Int?,
    val userId: String,
    val memberName: String?,
    val profileImage: String?
) {
    companion object {
        fun of(
            applicant: BusApplicantEntity,
            memberName: String?,
            profileImage: String?
        ): MemberWithBusApplicantResponse {
            return MemberWithBusApplicantResponse(
                boardingType = applicant.boardingType,
                seat = applicant.seat,
                userId = applicant.userId,
                memberName = memberName,
                profileImage = profileImage
            )
        }
    }
}
