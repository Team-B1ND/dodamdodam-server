package com.b1nd.dodamdodam.user.application.user.data.request

import jakarta.validation.constraints.NotBlank

data class VerifyPasswordRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val password: String
)