package com.b1nd.dodamdodam.inapp.application.app.data.response

import java.util.UUID

data class ActiveAppResponse(
    val appId: UUID,
    val name: String,
    val subtitle: String,
    val description: String?,
    val iconUrl: String,
    val darkIconUrl: String?,
)