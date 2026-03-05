package com.b1nd.dodamdodam.user.application.user.data.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ConfirmPhoneVerificationRequest(
    @NotBlank
    val phone: String,
    @field:Pattern(regexp = "\\d{6}")
    val code: String
)
