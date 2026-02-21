package com.b1nd.dodamdodam.bus.application.bus.data.response

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusEntity

data class BusResponse(
    val id: Long,
    val name: String
) {
    companion object {
        fun of(bus: BusEntity): BusResponse {
            return BusResponse(
                id = bus.id!!,
                name = bus.name
            )
        }
    }
}
