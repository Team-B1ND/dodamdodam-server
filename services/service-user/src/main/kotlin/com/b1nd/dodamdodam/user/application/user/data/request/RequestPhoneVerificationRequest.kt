package com.b1nd.dodamdodam.user.application.user.data.request

import jakarta.validation.constraints.NotBlank

data class RequestPhoneVerificationRequest(
    @NotBlank
    val phone: String
)
