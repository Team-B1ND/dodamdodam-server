package com.b1nd.dodamdodam.inapp.domain.app.command

import java.util.UUID

data class EditAppCommand(
    val appId: UUID,
    val name: String?,
    val subtitle: String?,
    val description: String?,
    val iconUrl: String?,
    val darkIconUrl: String?,
    val inquiryMail: String?,
    val server: EditServerCommand? = null,
)
