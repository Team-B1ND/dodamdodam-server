package com.b1nd.dodamdodam.inapp.domain.team.service

import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamMemberEntity
import com.b1nd.dodamdodam.inapp.domain.team.exception.TeamNameAlreadyExistException
import com.b1nd.dodamdodam.inapp.domain.team.exception.TeamNotFoundException
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamMemberRepository
import com.b1nd.dodamdodam.inapp.domain.team.repository.TeamRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TeamService(
    private val repository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository
) {
    fun getByName(name: String) =
        repository.findByName(name)
            ?: throw TeamNotFoundException()

    fun getById(publicId: UUID) =
        repository.findByPublicId(publicId)
            ?: throw TeamNotFoundException()

    fun delete(publicId: UUID) {
        val teamEntity = getById(publicId)
        teamMemberRepository.deleteAllByTeam(teamEntity)
        repository.delete(teamEntity)
    }


    fun create(userId: UUID, teamEntity: TeamEntity) {
        if (repository.existsByName(teamEntity.name))
            throw TeamNameAlreadyExistException()
        val savedEntity = repository.save(teamEntity)
        teamMemberRepository.save(TeamMemberEntity(savedEntity, userId))
    }

    fun addMember(team: TeamEntity, userIds: List<UUID>) {
        teamMemberRepository.saveAll(userIds.map { TeamMemberEntity(team, it) })
    }

    fun updateInfo(teamId: UUID, name: String?, description: String?, iconUrl: String?, githubUrl: String?) {
        val team = getById(teamId)
        name?.let {
            if (repository.existsByName(it)) {
                throw TeamNameAlreadyExistException()
            }
        }
        team.update(name, description, iconUrl, githubUrl)
    }

    fun deleteMember(teamId: UUID, users: List<UUID>) {
        val team = getById(teamId)
        teamMemberRepository.deleteAllByTeamAndUserIn(team, users)
    }
}