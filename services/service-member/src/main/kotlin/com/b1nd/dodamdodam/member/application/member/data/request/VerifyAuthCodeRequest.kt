package com.b1nd.dodamdodam.member.application.member.data.request

data class VerifyAuthCodeRequest(
    val identifier: String,
    val authCode: Int,
    val phone: String? = null,
)
