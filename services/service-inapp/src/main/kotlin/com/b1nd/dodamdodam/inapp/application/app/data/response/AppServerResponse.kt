package com.b1nd.dodamdodam.inapp.application.app.data.response

import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppStatusType

data class AppServerResponse(
    val name: String,
    val serverAddress: String,
    val redirectPath: String,
    val prefixLevel: Int,
    val omitApiPrefix: Boolean,
    val usePushNotification: Boolean,
    val enabled: Boolean,
    val status: AppStatusType,
    val denyResult: String?,
)
