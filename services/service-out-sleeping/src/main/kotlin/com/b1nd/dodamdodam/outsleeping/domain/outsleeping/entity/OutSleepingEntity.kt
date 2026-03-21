package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingAlreadyProcessedException
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(
    name = "out_sleepings",
    indexes = [Index(name = "idx_out_sleepings_start_at_end_at", columnList = "start_at, end_at")]
)
class OutSleepingEntity(

    @Column(name = "fk_user_id", nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(nullable = false)
    var reason: String,

    @Column(nullable = false)
    var startAt: LocalDate,

    @Column(nullable = false)
    var endAt: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: OutSleepingStatus = OutSleepingStatus.PENDING,

    @Column(name = "deny_reason")
    var denyReason: String? = null,
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun allow() {
        validatePending()
        this.status = OutSleepingStatus.ALLOWED
    }

    fun deny(denyReason: String?) {
        validatePending()
        this.status = OutSleepingStatus.DENIED
        this.denyReason = denyReason
    }

    fun revert() {
        if (this.status == OutSleepingStatus.PENDING) {
            throw OutSleepingAlreadyProcessedException()
        }
        this.status = OutSleepingStatus.PENDING
        this.denyReason = null
    }

    fun update(reason: String, startAt: LocalDate, endAt: LocalDate) {
        validatePending()
        this.reason = reason
        this.startAt = startAt
        this.endAt = endAt
    }

    private fun validatePending() {
        if (this.status != OutSleepingStatus.PENDING) {
            throw OutSleepingAlreadyProcessedException()
        }
    }
}
