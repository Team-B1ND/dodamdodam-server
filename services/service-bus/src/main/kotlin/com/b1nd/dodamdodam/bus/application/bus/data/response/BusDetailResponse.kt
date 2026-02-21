package com.b1nd.dodamdodam.bus.application.bus.data.response

data class BusDetailResponse(
    val id: Long,
    val name: String,
    val users: List<MemberWithBusApplicantResponse>
) {
    companion object {
        fun of(id: Long, name: String, users: List<MemberWithBusApplicantResponse>): BusDetailResponse {
            return BusDetailResponse(
                id = id,
                name = name,
                users = users
            )
        }
    }
}
