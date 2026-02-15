package com.b1nd.dodamdodam.user.application.user.data.request

data class StudentRegisterRequest(
    val username: String,
    val name: String,
    val password: String,
    val phone: String,
    val grade: Int,
    val room: Int,
    val number: Int
)