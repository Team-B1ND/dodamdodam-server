package com.b1nd.dodamdodam.gateway.domain.route.repository

import com.b1nd.dodamdodam.gateway.domain.route.entity.RouteEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RouteRepository: CoroutineCrudRepository<RouteEntity, Long> {
    fun findAllByEnabled(enabled: Boolean): Flow<RouteEntity>
    suspend fun findByServiceId(serviceId: UUID): RouteEntity?
}
