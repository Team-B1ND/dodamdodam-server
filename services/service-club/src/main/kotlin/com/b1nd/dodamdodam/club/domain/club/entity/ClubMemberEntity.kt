package com.b1nd.dodamdodam.club.domain.club.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "club_members")
class ClubMemberEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_club_id")
    val club: ClubEntity,
    @Column(name = "fk_user_id")
    val userId: UUID,
    var isOwner: Boolean,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}