package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "night_studies")
class NightStudyEntity(

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(nullable = false, length = 250)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val type: NightStudyType = NightStudyType.NIGHT_STUDY_2,

    @Column(nullable = false)
    val doNeedPhone: Boolean = false,

    val reasonForPhone: String? = null,

    @Column(nullable = false)
    val startAt: LocalDate,

    @Column(nullable = false)
    val endAt: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: NightStudyStatus = NightStudyStatus.PENDING,

    var rejectReason: String? = null
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun allow() {
        this.status = NightStudyStatus.ALLOWED
        this.rejectReason = null
    }

    fun reject(reason: String?) {
        this.status = NightStudyStatus.REJECTED
        this.rejectReason = reason
    }

    fun revert() {
        this.status = NightStudyStatus.PENDING
        this.rejectReason = null
    }
}
