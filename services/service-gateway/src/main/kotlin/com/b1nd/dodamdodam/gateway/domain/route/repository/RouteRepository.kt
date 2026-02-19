package com.b1nd.dodamdodam.gateway.domain.route.repository

import com.b1nd.dodamdodam.gateway.domain.route.entity.RouteEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RouteRepository: CoroutineCrudRepository<RouteEntity, Long> {
    fun findAllByEnabled(enabled: Boolean): Flow<RouteEntity>
}
