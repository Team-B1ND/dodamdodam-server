package com.b1nd.dodamdodam.gateway.infrastructure.kafka.consumer

import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.app.AppServerRouteEvent
import com.b1nd.dodamdodam.core.kafka.event.app.AppServerRouteEventType
import com.b1nd.dodamdodam.gateway.domain.route.entity.RouteEntity
import com.b1nd.dodamdodam.gateway.domain.route.repository.RouteRepository
import kotlinx.coroutines.runBlocking
import org.springframework.cloud.gateway.event.RefreshRoutesEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AppServerRouteEventConsumer(
    private val routeRepository: RouteRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @KafkaListener(
        topics = [KafkaTopics.APP_SERVER_ROUTE_CHANGED],
        groupId = "service-gateway-route",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consume(event: AppServerRouteEvent) {
        runBlocking {
            upsertRoute(event)
            eventPublisher.publishEvent(RefreshRoutesEvent(this))
        }
    }

    private suspend fun upsertRoute(event: AppServerRouteEvent) {
        val existed = routeRepository.findByServiceId(event.appId)
        routeRepository.save(buildRoute(event, existed))
    }

    private fun buildRoute(event: AppServerRouteEvent, existed: RouteEntity?): RouteEntity =
        existed?.let { updateRoute(it, event) } ?: createRoute(event)

    private fun createRoute(event: AppServerRouteEvent) = RouteEntity(
        id = null,
        serviceId = event.appId,
        path = event.path,
        targetUri = event.targetUri,
        stripPrefix = event.stripPrefix,
        enabled = event.enabled,
        createdAt = null,
        modifiedAt = null,
    )

    private fun updateRoute(existed: RouteEntity, event: AppServerRouteEvent) = existed.copy(
        path = event.path,
        targetUri = event.targetUri,
        stripPrefix = event.stripPrefix,
        enabled = resolveEnabled(event),
    )

    private fun resolveEnabled(event: AppServerRouteEvent): Boolean =
        when (event.eventType) {
            AppServerRouteEventType.CREATED -> true
            AppServerRouteEventType.UPDATED -> event.enabled
        }
}
