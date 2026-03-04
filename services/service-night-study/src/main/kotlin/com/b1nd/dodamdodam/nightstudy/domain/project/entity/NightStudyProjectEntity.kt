package com.b1nd.dodamdodam.nightstudy.domain.project.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.project.enumeration.NightStudyProjectType
import com.b1nd.dodamdodam.nightstudy.domain.project.enumeration.ProjectRoom
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "night_study_projects")
class NightStudyProjectEntity(

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val type: NightStudyProjectType,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = false)
    val startAt: LocalDate,

    @Column(nullable = false)
    val endAt: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: NightStudyStatus = NightStudyStatus.PENDING,

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var room: ProjectRoom? = null,

    var rejectReason: String? = null,

    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL], orphanRemoval = true)
    val members: MutableList<NightStudyProjectMemberEntity> = mutableListOf()
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun allow(room: ProjectRoom) {
        this.status = NightStudyStatus.ALLOWED
        this.room = room
        this.rejectReason = null
    }

    fun reject(reason: String?) {
        this.status = NightStudyStatus.REJECTED
        this.rejectReason = reason
    }

    fun revert() {
        this.status = NightStudyStatus.PENDING
        this.room = null
        this.rejectReason = null
    }
}
