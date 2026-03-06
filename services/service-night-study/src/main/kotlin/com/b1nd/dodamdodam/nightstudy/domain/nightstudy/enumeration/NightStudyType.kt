package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration

enum class NightStudyType {
    NIGHT_STUDY_1,
    NIGHT_STUDY_2,
    NIGHT_STUDY_3,
    PROJECT_1,
    PROJECT_2;

    fun isProject(): Boolean = this == PROJECT_1 || this == PROJECT_2
}
