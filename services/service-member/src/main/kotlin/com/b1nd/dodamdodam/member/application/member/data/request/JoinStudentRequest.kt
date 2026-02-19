package com.b1nd.dodamdodam.member.application.member.data.request

data class JoinStudentRequest(
    val id: String,
    val password: String,
    val name: String,
    val email: String,
    val phone: String,
    val grade: Int,
    val room: Int,
    val number: Int,
)
