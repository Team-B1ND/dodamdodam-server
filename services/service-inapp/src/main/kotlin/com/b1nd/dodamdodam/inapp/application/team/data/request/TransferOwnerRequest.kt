package com.b1nd.dodamdodam.inapp.application.team.data.request

import java.util.UUID

data class TransferOwnerRequest(
    val teamId: UUID,
    val userPublicId: UUID
)
