package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "night_study_members")
class NightStudyMemberEntity(

    @Column(name = "night_study_id", nullable = false)
    val nightStudyId: Long,

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
