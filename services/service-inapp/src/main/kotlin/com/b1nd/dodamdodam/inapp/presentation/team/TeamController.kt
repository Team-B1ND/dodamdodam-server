package com.b1nd.dodamdodam.inapp.presentation.team

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.inapp.application.team.TeamUseCase
import com.b1nd.dodamdodam.inapp.application.team.data.request.AddTeamMemberRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.CreateTeamRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.EditTeamInfoRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/team")
class TeamController(
    private val teamUseCase: TeamUseCase
) {
    @UserAccess
    @PostMapping
    fun createTeam(@RequestBody request: CreateTeamRequest) =
        teamUseCase.createTeam(request)

    @UserAccess
    @PatchMapping
    fun editTeam(@RequestBody request: EditTeamInfoRequest) =
        teamUseCase.editTeamInfo(request)

    @UserAccess
    @GetMapping("/me")
    fun getMyTeam() =
        teamUseCase.getMyTeam()

    @UserAccess
    @GetMapping("/{teamId}")
    fun getTeam(@PathVariable teamId: UUID) =
        teamUseCase.getTeam(teamId)

    @UserAccess
    @DeleteMapping("/{teamId}")
    fun deleteTeam(@PathVariable teamId: UUID) =
        teamUseCase.deleteTeam(teamId)

    @UserAccess
    @PostMapping("/member")
    fun addMembers(request: AddTeamMemberRequest) =
        teamUseCase.addTeamMembers(request)

    @UserAccess
    @GetMapping("/{teamId}/member")
    fun getMembers(@PathVariable teamId: UUID) =
        teamUseCase.getTeamMembers(teamId)

    @UserAccess
    @DeleteMapping("/{teamId}/member")
    fun deleteMembers(
        @PathVariable teamId: UUID,
        @RequestParam users: List<UUID>
    ) = teamUseCase.deleteTeamMembers(teamId, users)
}