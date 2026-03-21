package com.b1nd.dodamdodam.inapp.application.app.data.request

data class EditAppServerInfoRequest(
    val name: String? = null,
    val serverAddress: String? = null,
    val redirectPath: String? = null,
    val omitApiPrefix: Boolean? = null,
    val usePushNotification: Boolean? = null,
)