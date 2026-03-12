package com.b1nd.dodamdodam.inapp.domain.app.command

import java.util.UUID

data class CreateServerCommand(
    val appId: UUID?,
    val name: String,
    val serverAddress: String,
    val redirectPath: String,
    val prefixLevel: Int,
    val usePushNotification: Boolean,
)
