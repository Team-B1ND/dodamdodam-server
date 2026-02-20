package com.b1nd.dodamdodam.bus.domain.bus.repository

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusApplicantEntity
import com.b1nd.dodamdodam.bus.domain.bus.entity.BusEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface BusApplicantRepository : JpaRepository<BusApplicantEntity, Long> {
    fun deleteAllByBusId(busId: Long)
    fun findAllByBus(bus: BusEntity): List<BusApplicantEntity>
    fun findByBusAndSeat(bus: BusEntity, seat: Int): BusApplicantEntity?

    @EntityGraph(attributePaths = ["bus"])
    fun findByUserId(userId: String): BusApplicantEntity?
}
