package com.b1nd.dodamdodam.club.domain.club.entity

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubTimeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "club_times")
class ClubTimeEntity(
    @Id
    @Enumerated(EnumType.STRING)
    val id: ClubTimeType,

    val start: LocalDate,

    val end: LocalDate,
)
