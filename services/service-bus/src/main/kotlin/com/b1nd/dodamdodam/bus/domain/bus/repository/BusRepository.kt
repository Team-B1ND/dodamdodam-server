package com.b1nd.dodamdodam.bus.domain.bus.repository

import com.b1nd.dodamdodam.bus.domain.bus.entity.BusEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BusRepository : JpaRepository<BusEntity, Long>
