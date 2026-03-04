package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class CreateAppRequest(
    val teamId: UUID,
    val name: String,
    val subtitle: String,
    val description: String? = null,
    val iconUrl: String,
    val darkIconUrl: String? = null,
    val inquiryMail: String,
)
