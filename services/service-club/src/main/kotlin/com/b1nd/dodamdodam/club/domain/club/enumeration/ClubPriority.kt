package com.b1nd.dodamdodam.club.domain.club.enumeration

enum class ClubPriority {
    CREATIVE_ACTIVITY_CLUB_1,
    CREATIVE_ACTIVITY_CLUB_2,
    CREATIVE_ACTIVITY_CLUB_3,
    ;

    companion object {
        fun getClubPriorities(): List<ClubPriority> =
            listOf(CREATIVE_ACTIVITY_CLUB_1, CREATIVE_ACTIVITY_CLUB_2, CREATIVE_ACTIVITY_CLUB_3)
    }
}
