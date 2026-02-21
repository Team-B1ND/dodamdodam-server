package com.b1nd.dodamdodam.club.application.club.data.response

import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity

data class ClubJoinStudentResponse(
    val student: ClubStudentResponse,
    val introduction: String?,
) {
    companion object {
        fun fromEntity(
            clubMember: ClubMemberEntity,
            name: String,
            grade: Int,
            room: Int,
            number: Int,
            profileImage: String?,
        ): ClubJoinStudentResponse =
            ClubJoinStudentResponse(
                student = ClubStudentResponse.fromEntity(
                    clubMember = clubMember,
                    name = name,
                    grade = grade,
                    room = room,
                    number = number,
                    profileImage = profileImage,
                ),
                introduction = clubMember.introduction,
            )
    }
}
