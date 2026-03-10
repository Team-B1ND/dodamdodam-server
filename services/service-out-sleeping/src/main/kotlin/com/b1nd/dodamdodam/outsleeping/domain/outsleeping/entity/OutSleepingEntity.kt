package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatusType
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "out_sleepings")
@SQLRestriction("is_deleted = false")
class OutSleepingEntity(

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(nullable = false)
    val reason: String,

    @Column(nullable = false)
    val startAt: LocalDate,

    @Column(nullable = false)
    val endAt: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: OutSleepingStatusType = OutSleepingStatusType.PENDING,

    @Column
    var rejectReason: String? = null,

    @Column(nullable = false)
    var isDeleted: Boolean = false
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun allow() {
        this.status = OutSleepingStatusType.ALLOWED
    }

    fun reject(reason: String?) {
        this.status = OutSleepingStatusType.REJECTED
        this.rejectReason = reason
    }

    fun revert() {
        this.status = OutSleepingStatusType.PENDING
        this.rejectReason = null
    }

    fun softDelete() {
        this.isDeleted = true
    }
}
