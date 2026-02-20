package com.b1nd.dodamdodam.member.application.member.data.request

data class UpdateMemberInfoRequest(
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val profileImage: String? = null,
)
