package com.b1nd.dodamdodam.club.domain.club.enumeration

enum class ClubStatus {
    ALLOWED,
    PENDING,
    REJECTED,
    WAITING,
    DELETED,
    ;

    companion object {
        fun getNotAllowedStatuses(): List<ClubStatus> = listOf(WAITING, REJECTED, DELETED)
    }
}
