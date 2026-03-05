package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class DenyAppServerRequest(
    val appId: UUID,
    val denyResult: String? = null,
)
