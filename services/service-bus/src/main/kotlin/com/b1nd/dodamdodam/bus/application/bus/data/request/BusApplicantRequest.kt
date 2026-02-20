package com.b1nd.dodamdodam.bus.application.bus.data.request

data class BusApplicantRequest(
    val userIds: List<String>,
    val busId: Long
)
