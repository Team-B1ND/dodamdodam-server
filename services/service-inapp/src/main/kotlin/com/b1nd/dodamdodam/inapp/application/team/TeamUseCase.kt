package com.b1nd.dodamdodam.inapp.application.team

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.inapp.application.team.data.request.AddTeamMemberRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.CreateTeamRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.DeleteTeamMemberRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.DeleteTeamRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.EditTeamInfoRequest
import com.b1nd.dodamdodam.inapp.domain.team.service.TeamService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class TeamUseCase(
    private val teamService: TeamService
) {
    fun createTeam(request: CreateTeamRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        teamService.create(userId, request.toTeamEntity())
        return Response.created("팀이 생성되었어요.")
    }

    fun addTeamMembers(request: AddTeamMemberRequest): Response<Any> {
        val team = teamService.getById(request.teamId)
        teamService.addMember(team, request.users)
        return Response.ok("팀 멤버가 추가되었어요.")
    }

    fun deleteTeamMembers(request: DeleteTeamMemberRequest): Response<Any> {
        teamService.deleteMember(request.teamId, request.users)
        return Response.ok("팀 멤버가 삭제되었어요.")
    }

    fun deleteTeam(request: DeleteTeamRequest): Response<Any> {
        teamService.delete(request.teamId)
        return Response.ok("팀이 삭제되었어요.")
    }

    fun editTeamInfo(request: EditTeamInfoRequest): Response<Any> {
        teamService.updateInfo(
            request.teamId,
            request.name,
            request.description,
            request.iconUrl,
            request.githubUrl
        )
        return Response.ok("팀 정보가 수정되었어요.")
    }
}