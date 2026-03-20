package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "night_study_banned")
class NightStudyBannedEntity (
    @Column(name = "fk_user_id", columnDefinition = "BINARY(16)")
    var userId: UUID,

    var reason: String,

    var endAt: LocalDate,

    var bannedAt: LocalDate,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}