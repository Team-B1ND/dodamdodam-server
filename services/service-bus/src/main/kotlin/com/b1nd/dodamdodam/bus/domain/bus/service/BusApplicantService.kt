package com.b1nd.dodamdodam.bus.domain.bus.service

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusApplicantEntity
import com.b1nd.dodamdodam.bus.domain.bus.entity.BusEntity
import com.b1nd.dodamdodam.bus.domain.bus.exception.BusAlreadyAppliedPositionException
import com.b1nd.dodamdodam.bus.domain.bus.exception.BusApplicantNotFoundException
import com.b1nd.dodamdodam.bus.domain.bus.repository.BusApplicantRepository
import org.springframework.stereotype.Service

@Service
class BusApplicantService(
    private val busApplicantRepository: BusApplicantRepository
) {
    fun getByBus(bus: BusEntity): List<BusApplicantEntity> {
        return busApplicantRepository.findAllByBus(bus)
    }

    fun checkSeatAvailable(bus: BusEntity, seat: Int) {
        if (busApplicantRepository.findByBusAndSeat(bus, seat) != null) {
            throw BusAlreadyAppliedPositionException()
        }
    }

    fun deleteByBusId(busId: Long) {
        busApplicantRepository.deleteAllByBusId(busId)
    }

    fun getByUserId(userId: String): BusApplicantEntity {
        return busApplicantRepository.findByUserId(userId)
            ?: throw BusApplicantNotFoundException()
    }

    fun getByUserIdOrNull(userId: String): BusApplicantEntity? {
        return busApplicantRepository.findByUserId(userId)
    }

    fun create(applicant: BusApplicantEntity) {
        busApplicantRepository.save(applicant)
    }

    fun createAll(applicants: List<BusApplicantEntity>) {
        busApplicantRepository.saveAll(applicants)
    }
}
