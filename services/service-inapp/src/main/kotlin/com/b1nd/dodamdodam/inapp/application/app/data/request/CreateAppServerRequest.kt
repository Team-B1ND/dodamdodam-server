package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class CreateAppServerRequest(
    val appId: UUID,
    val name: String,
    val serverAddress: String,
    val redirectPath: String,
    val omitApiPrefix: Boolean,
    val usePushNotification: Boolean,
)
