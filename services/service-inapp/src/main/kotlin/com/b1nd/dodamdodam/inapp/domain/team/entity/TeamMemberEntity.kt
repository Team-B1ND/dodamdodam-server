package com.b1nd.dodamdodam.inapp.domain.team.entity

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
@Table(name = "team_members")
class TeamMemberEntity(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_team_id", nullable = false)
    val team: TeamEntity,

    @Column(name = "fk_user_id", nullable = false)
    val user: UUID,

    var isOwner: Boolean = false
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
