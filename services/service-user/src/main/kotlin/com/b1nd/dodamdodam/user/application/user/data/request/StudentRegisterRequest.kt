package com.b1nd.dodamdodam.user.application.user.data.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class StudentRegisterRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val name: String,
    @NotBlank
    val password: String,
    @NotBlank
    val phone: String,
    @NotEmpty
    val grade: Int,
    @NotEmpty
    val room: Int,
    @NotEmpty
    val number: Int
)