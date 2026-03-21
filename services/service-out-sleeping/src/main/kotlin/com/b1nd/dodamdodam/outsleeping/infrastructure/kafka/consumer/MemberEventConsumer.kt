package com.b1nd.dodamdodam.outsleeping.infrastructure.kafka.consumer

import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.user.UserCreatedEvent
import com.b1nd.dodamdodam.core.kafka.event.user.UserUpdatedEvent
import com.b1nd.dodamdodam.outsleeping.domain.member.service.MemberService
import jakarta.transaction.Transactional
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Transactional(rollbackOn = [Exception::class])
class MemberEventConsumer(
    private val memberService: MemberService
) {

    @KafkaListener(
        topics = [KafkaTopics.USER_CREATED],
        groupId = "service-out-sleeping-created",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeUserCreated(event: UserCreatedEvent) {
        val userId = event.publicId ?: return
        memberService.createOrUpdate(
            userId = userId,
            name = event.name,
            role = event.role,
            grade = event.grade,
            room = event.room,
            number = event.number,
        )
    }

    @KafkaListener(
        topics = [KafkaTopics.USER_UPDATED],
        groupId = "service-out-sleeping-updated",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeUserUpdated(event: UserUpdatedEvent) {
        val userId = event.publicId ?: return
        val role = event.roles.firstOrNull() ?: return
        memberService.createOrUpdate(
            userId = userId,
            name = event.name,
            role = role,
            grade = event.grade,
            room = event.room,
            number = event.number,
        )
    }
}
