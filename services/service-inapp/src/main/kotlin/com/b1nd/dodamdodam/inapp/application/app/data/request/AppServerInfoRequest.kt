package com.b1nd.dodamdodam.inapp.application.app.data.request

data class AppServerInfoRequest(
    val name: String,
    val serverAddress: String,
    val redirectPath: String,
    val omitApiPrefix: Boolean,
    val usePushNotification: Boolean,
)
