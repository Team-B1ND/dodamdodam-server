package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import java.util.UUID

data class MemberResponse(
    val id: UUID,
    val name: String,
    val student: StudentResponse?,
)
