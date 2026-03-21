package com.b1nd.dodamdodam.inapp.domain.team.repository

import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TeamMemberRepository: JpaRepository<TeamMemberEntity, Long> {
    fun deleteAllByTeam(team: TeamEntity)
    fun deleteAllByTeamAndUserIn(team: TeamEntity, users: List<UUID>)
    fun existsByUserAndTeam(user: UUID, team: TeamEntity): Boolean
    fun existsByUserAndTeamAndIsOwnerIsTrue(user: UUID, team: TeamEntity): Boolean
    fun existsByUserInAndTeamAndIsOwnerIsTrue(users: List<UUID>, team: TeamEntity): Boolean
    fun findByUserAndTeam(user: UUID, team: TeamEntity): TeamMemberEntity?
    fun findAllByUser(user: UUID): MutableList<TeamMemberEntity>
    fun findAllByTeam(team: TeamEntity): List<TeamMemberEntity>
}
