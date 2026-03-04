package com.b1nd.dodamdodam.nightstudy.domain.project.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "night_study_project_members")
class NightStudyProjectMemberEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    val project: NightStudyProjectEntity,

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
