package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatusType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "out_sleepings")
class OutSleepingEntity(

    @Column(name = "fk_student_id", nullable = false)
    val studentId: UUID,

    @Column(nullable = false)
    val reason: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OutSleepingStatusType = OutSleepingStatusType.PENDING,

    var rejectReason: String? = null,

    @Column(nullable = false)
    val startAt: LocalDate,

    @Column(nullable = false)
    val endAt: LocalDate

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
