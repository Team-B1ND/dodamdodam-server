package com.b1nd.dodamdodam.inapp.domain.app.repository

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AppRepository: JpaRepository<AppEntity, Long> {
    fun existsByName(name: String): Boolean
    fun findByPublicId(publicId: UUID): AppEntity?
    fun findAllByTeamOrderByIdDesc(team: TeamEntity): List<AppEntity>
    fun findAllByTeamInOrderByIdDesc(teams: List<TeamEntity>): List<AppEntity>
}
