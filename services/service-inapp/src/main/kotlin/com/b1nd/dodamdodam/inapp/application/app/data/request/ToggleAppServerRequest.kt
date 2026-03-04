package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class ToggleAppServerRequest(
    val appId: UUID,
    val enabled: Boolean,
)
