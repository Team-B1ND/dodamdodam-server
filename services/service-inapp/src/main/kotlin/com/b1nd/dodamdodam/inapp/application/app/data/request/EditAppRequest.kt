package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class EditAppRequest(
    val appId: UUID,
    val name: String? = null,
    val subtitle: String? = null,
    val description: String? = null,
    val iconUrl: String? = null,
    val darkIconUrl: String? = null,
    val inquiryMail: String? = null,
)
