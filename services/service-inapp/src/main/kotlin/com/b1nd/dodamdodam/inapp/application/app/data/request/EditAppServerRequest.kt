package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class EditAppServerRequest(
    val appId: UUID,
    val name: String? = null,
    val serverAddress: String? = null,
    val redirectPath: String? = null,
    val omitApiPrefix: Boolean? = null,
    val usePushNotification: Boolean? = null,
)
