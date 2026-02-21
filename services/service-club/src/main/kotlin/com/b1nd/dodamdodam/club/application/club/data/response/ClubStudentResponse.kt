package com.b1nd.dodamdodam.club.application.club.data.response

import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPermission
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus

data class ClubStudentResponse(
    val id: Long,
    val status: ClubStatus,
    val permission: ClubPermission,
    val studentId: Long,
    val name: String,
    val grade: Int,
    val room: Int,
    val number: Int,
    val profileImage: String?,
) {
    companion object {
        fun fromEntity(
            clubMember: ClubMemberEntity,
            name: String,
            grade: Int,
            room: Int,
            number: Int,
            profileImage: String?,
        ): ClubStudentResponse =
            ClubStudentResponse(
                id = clubMember.id!!,
                status = clubMember.clubStatus,
                permission = clubMember.permission,
                studentId = clubMember.studentId,
                name = name,
                grade = grade,
                room = room,
                number = number,
                profileImage = profileImage,
            )
    }
}
