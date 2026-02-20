package com.b1nd.dodamdodam.member.application.member.data.request

data class JoinTeacherRequest(
    val id: String,
    val password: String,
    val name: String,
    val email: String,
    val phone: String,
    val tel: String,
    val position: String,
)
