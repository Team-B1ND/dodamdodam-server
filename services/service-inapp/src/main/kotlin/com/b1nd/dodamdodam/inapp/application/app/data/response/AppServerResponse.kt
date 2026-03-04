package com.b1nd.dodamdodam.inapp.application.app.data.response

import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppServerStatusType

data class AppServerResponse(
    val name: String,
    val serverAddress: String,
    val redirectPath: String,
    val prefixLevel: Int,
    val omitApiPrefix: Boolean,
    val enabled: Boolean,
    val status: AppServerStatusType,
    val denyResult: String?,
)
