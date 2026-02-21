package com.b1nd.dodamdodam.club.domain.club.entity

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPermission
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPriority
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "club_members")
class ClubMemberEntity(
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val permission: ClubPermission,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var clubStatus: ClubStatus,

    @Enumerated(EnumType.STRING)
    var priority: ClubPriority? = null,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_club_id", nullable = false)
    val club: ClubEntity,

    @Column(columnDefinition = "TEXT")
    val introduction: String? = null,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun isFirstChoice(): Boolean = priority == ClubPriority.CREATIVE_ACTIVITY_CLUB_1

    fun modifyStatus(status: ClubStatus) {
        this.clubStatus = status
    }
}
