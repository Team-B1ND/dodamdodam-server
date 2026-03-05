package com.b1nd.dodamdodam.inapp.presentation.team

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.inapp.application.team.TeamUseCase
import com.b1nd.dodamdodam.inapp.application.team.data.request.AddTeamMemberRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.CreateTeamRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.DeleteTeamMemberRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.DeleteTeamRequest
import com.b1nd.dodamdodam.inapp.application.team.data.request.EditTeamInfoRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/team")
class TeamController(
    private val teamUseCase: TeamUseCase
) {
    @UserAccess
    @PostMapping
    fun createTeam(request: CreateTeamRequest) =
        teamUseCase.createTeam(request)

    @UserAccess
    @PatchMapping
    fun editTeam(request: EditTeamInfoRequest) =
        teamUseCase.editTeamInfo(request)

    @UserAccess
    @GetMapping("/me")
    fun getMyTeam() =
        teamUseCase.getMyTeam()

    @UserAccess
    @DeleteMapping
    fun deleteTeam(request: DeleteTeamRequest) =
        teamUseCase.deleteTeam(request)

    @UserAccess
    @PostMapping("/member")
    fun addMembers(request: AddTeamMemberRequest) =
        teamUseCase.addTeamMembers(request)

    @UserAccess
    @DeleteMapping("/member")
    fun deleteMembers(request: DeleteTeamMemberRequest) =
        teamUseCase.deleteTeamMembers(request)
}