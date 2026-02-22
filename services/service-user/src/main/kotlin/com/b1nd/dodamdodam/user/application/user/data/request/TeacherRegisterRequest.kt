package com.b1nd.dodamdodam.user.application.user.data.request

import jakarta.validation.constraints.NotBlank

data class TeacherRegisterRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val name: String,
    @NotBlank
    val password: String,
    @NotBlank
    val phone: String,
    @NotBlank
    val position: String
)
