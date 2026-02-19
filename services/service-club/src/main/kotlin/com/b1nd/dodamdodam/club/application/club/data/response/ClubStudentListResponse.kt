package com.b1nd.dodamdodam.club.application.club.data.response

data class ClubStudentListResponse(
    val isLeader: Boolean,
    val students: List<ClubStudentResponse>,
) {
    companion object {
        fun of(isLeader: Boolean, students: List<ClubStudentResponse>): ClubStudentListResponse =
            ClubStudentListResponse(
                isLeader = isLeader,
                students = students,
            )
    }
}
