package com.b1nd.dodamdodam.inapp.presentation.team

import com.b1nd.dodamdodam.inapp.application.team.TeamUseCase
import org.springframework.web.bind.annotation.RestController

@RestController
class TeamController(
    private val teamUseCase: TeamUseCase
) {
    fun createTeam() {

    }
}