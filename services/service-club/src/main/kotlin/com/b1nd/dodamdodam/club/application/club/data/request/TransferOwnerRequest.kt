package com.b1nd.dodamdodam.club.application.club.data.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class TransferOwnerRequest(
    @NotNull
    val targetUserId: UUID,
)
