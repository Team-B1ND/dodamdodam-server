package com.b1nd.dodamdodam.outgoing.application.outgoing.data.request

import org.springframework.lang.Nullable

data class RejectRequest(
    @field:Nullable
    val rejectReason: String?
)
