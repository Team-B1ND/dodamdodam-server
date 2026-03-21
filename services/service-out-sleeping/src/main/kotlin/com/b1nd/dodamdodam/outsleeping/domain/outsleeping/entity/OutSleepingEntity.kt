package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "out_sleepings")
class OutSleepingEntity(

    @Column(nullable = false, columnDefinition = "BINARY(16)")
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

    var rejectReason: String? = null,
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun allow() {
        this.status = OutSleepingStatus.ALLOWED
    }

    fun reject(rejectReason: String?) {
        this.status = OutSleepingStatus.REJECTED
        this.rejectReason = rejectReason
    }

    fun revert() {
        this.status = OutSleepingStatus.PENDING
        this.rejectReason = null
    }

    fun update(reason: String, startAt: LocalDate, endAt: LocalDate) {
        this.reason = reason
        this.startAt = startAt
        this.endAt = endAt
    }
}
