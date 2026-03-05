package com.b1nd.dodamdodam.gateway.infrastructure.gateway.scheduler

import org.springframework.cloud.gateway.event.RefreshRoutesEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RouteRefreshScheduler(
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Scheduled(fixedDelay = 120000)
    fun refreshRoutesPeriodically() {
        eventPublisher.publishEvent(RefreshRoutesEvent(this))
    }
}
