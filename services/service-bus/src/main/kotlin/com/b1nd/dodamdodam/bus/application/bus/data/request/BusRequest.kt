package com.b1nd.dodamdodam.bus.application.bus.data.request

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusEntity

data class BusRequest(
    val name: String
) {
    fun toEntity(): BusEntity {
        return BusEntity(name = name)
    }
}
