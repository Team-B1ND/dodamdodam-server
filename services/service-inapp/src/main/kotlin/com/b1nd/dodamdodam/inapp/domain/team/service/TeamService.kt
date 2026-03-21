package com.b1nd.dodamdodam.inapp.domain.team.service

import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamMemberEntity
import com.b1nd.dodamdodam.inapp.domain.team.exception.TeamCannotRemoveOwnerException
import com.b1nd.dodamdodam.inapp.domain.team.exception.TeamMemberNotFoundException
import com.b1nd.dodamdodam.inapp.domain.team.exception.TeamNameAlreadyExistException
import com.b1nd.dodamdodam.inapp.domain.team.exception.TeamNotFoundException
import com.b1nd.dodamdodam.inapp.domain.team.exception.TeamOwnerPermissionRequiredException
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

    fun validateOwner(userId: UUID, teamId: UUID) {
        val team = getById(teamId)
        if (!existsOwner(userId, team))
            throw TeamOwnerPermissionRequiredException()
    }

    fun existsOwner(userId: UUID, teamEntity: TeamEntity) =
        teamMemberRepository.existsByUserAndTeamAndIsOwnerIsTrue(userId, teamEntity)

    fun create(userId: UUID, teamEntity: TeamEntity): TeamEntity {
        if (repository.existsByName(teamEntity.name))
            throw TeamNameAlreadyExistException()
        val savedEntity = repository.save(teamEntity)
        teamMemberRepository.save(TeamMemberEntity(savedEntity, userId, true))
        return savedEntity
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
        if (teamMemberRepository.existsByUserInAndTeamAndIsOwnerIsTrue(users, team)) {
            throw TeamCannotRemoveOwnerException()
        }
        teamMemberRepository.deleteAllByTeamAndUserIn(team, users)
    }

    fun transferOwner(teamId: UUID, currentOwnerId: UUID, newOwnerId: UUID) {
        val team = getById(teamId)
        val currentOwner = teamMemberRepository.findByUserAndTeam(currentOwnerId, team)
            ?: throw TeamMemberNotFoundException()
        val newOwner = teamMemberRepository.findByUserAndTeam(newOwnerId, team)
            ?: throw TeamMemberNotFoundException()
        currentOwner.isOwner = false
        newOwner.isOwner = true
    }

    fun getAllByUser(userId: UUID): List<TeamMemberEntity> =
        teamMemberRepository.findAllByUser(userId)

    fun getAllByTeam(teamId: UUID): List<TeamMemberEntity> =
        teamMemberRepository.findAllByTeam(getById(teamId))
}
