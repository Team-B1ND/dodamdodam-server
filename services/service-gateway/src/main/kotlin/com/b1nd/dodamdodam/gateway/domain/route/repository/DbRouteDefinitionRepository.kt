package com.b1nd.dodamdodam.gateway.domain.route.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.asFlux
import org.springframework.cloud.gateway.filter.FilterDefinition
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition
import org.springframework.cloud.gateway.route.RouteDefinition
import org.springframework.cloud.gateway.route.RouteDefinitionRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@Component
class DbRouteDefinitionRepository(
    private val repository: RouteRepository
): RouteDefinitionRepository {
    override fun getRouteDefinitions(): Flux<RouteDefinition> {
        return repository.findAllByEnabled(true)
            .map { entity ->
                RouteDefinition().apply {
                    id = entity.serviceId.toString()
                    uri = URI.create(entity.targetUri)

                    predicates = listOf(
                        PredicateDefinition("Path=${entity.path}")
                    )

                    filters = listOf(
                        FilterDefinition("StripPrefix=${entity.stripPrefix}")
                    )
                }
            }.asFlux()
    }

    override fun save(route: Mono<RouteDefinition>): Mono<Void> = Mono.empty()
    override fun delete(routeId: Mono<String>): Mono<Void> = Mono.empty()

}