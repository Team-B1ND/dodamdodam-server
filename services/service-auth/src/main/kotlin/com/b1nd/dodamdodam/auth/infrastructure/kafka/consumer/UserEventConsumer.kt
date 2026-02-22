package com.b1nd.dodamdodam.auth.infrastructure.kafka.consumer

import com.b1nd.dodamdodam.auth.domain.principal.service.PrincipalService
import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.user.UserCreatedEvent
import com.b1nd.dodamdodam.core.kafka.event.user.UserUpdatedEvent
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import jakarta.transaction.Transactional
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Transactional(rollbackOn = [Exception::class])
class UserEventConsumer(
    private val principalService: PrincipalService
) {
    @KafkaListener(
        topics = [KafkaTopics.USER_CREATED],
        groupId = "service-auth-created",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeUserCreated(event: UserCreatedEvent) {
        val userId = event.publicId ?: return
        principalService.updatePrincipal(
            userId = userId,
            status = event.status,
            username = event.username,
            roles = setOf(parseRole(event.role))
        )
    }

    @KafkaListener(
        topics = [KafkaTopics.USER_UPDATED],
        groupId = "service-auth-updated",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeUserUpdated(event: UserUpdatedEvent) {
        val userId = event.publicId ?: return
        principalService.updatePrincipal(
            userId = userId,
            status = event.status,
            username = event.username,
            roles = parseRoles(event.roles)
        )
    }

    private fun parseRole(rawRole: String): RoleType =
        runCatching { RoleType.valueOf(rawRole) }
            .getOrElse { RoleType.STUDENT }

    private fun parseRoles(rawRoles: Collection<String>): Set<RoleType> {
        val parsedRoles = rawRoles.mapNotNull { rawRole ->
            runCatching { RoleType.valueOf(rawRole) }.getOrNull()
        }.toSet()

        return parsedRoles.ifEmpty {emptySet()}
    }
}
