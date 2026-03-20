package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "night_study_members")
class NightStudyMemberEntity (
    @Column(name = "fk_night_study_id")
    var nightStudyId: Long,
    @Column(name = "fk_user_id", columnDefinition = "BINARY(16)")
    var userId: UUID,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}