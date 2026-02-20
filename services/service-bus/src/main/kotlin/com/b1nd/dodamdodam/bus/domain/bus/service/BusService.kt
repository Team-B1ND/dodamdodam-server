package com.b1nd.dodamdodam.bus.domain.bus.service

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusEntity
import com.b1nd.dodamdodam.bus.domain.bus.exception.BusNotFoundException
import com.b1nd.dodamdodam.bus.domain.bus.repository.BusApplicantRepository
import com.b1nd.dodamdodam.bus.domain.bus.repository.BusRepository
import org.springframework.stereotype.Service

@Service
class BusService(
    private val busRepository: BusRepository,
    private val busApplicantRepository: BusApplicantRepository
) {
    fun getAll(): List<BusEntity> {
        return busRepository.findAll()
    }

    fun getById(id: Long): BusEntity {
        return busRepository.findById(id)
            .orElseThrow { BusNotFoundException() }
    }

    fun create(bus: BusEntity): BusEntity {
        return busRepository.save(bus)
    }

    fun deleteById(busId: Long) {
        busApplicantRepository.deleteAllByBusId(busId)
        busRepository.deleteById(busId)
    }
}
