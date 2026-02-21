package com.b1nd.dodamdodam.user.application.user.data.request

data class UpdateUserRequest(
    val name: String,
    val phone: String?,
    val profileImage: String?
)
