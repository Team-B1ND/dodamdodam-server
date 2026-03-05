package com.b1nd.dodamdodam.user.infrastructure.sms.data

import com.fasterxml.jackson.annotation.JsonProperty

data class GabiaTokenResponse(
    @field:JsonProperty("access_token")
    val accessToken: String,
    @field:JsonProperty("expires_in")
    val expiresIn: Long? = null
)
