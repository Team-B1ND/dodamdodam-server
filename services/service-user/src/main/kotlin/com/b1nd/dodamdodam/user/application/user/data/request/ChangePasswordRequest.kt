package com.b1nd.dodamdodam.user.application.user.data.request

import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @NotBlank
    val postPassword: String,
    @NotBlank
    val newPassword: String
)
