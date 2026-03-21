package com.b1nd.dodamdodam.user.application.user.data.request

data class ResetPasswordRequest(
    val phone: String,
    val newPassword: String,
)
