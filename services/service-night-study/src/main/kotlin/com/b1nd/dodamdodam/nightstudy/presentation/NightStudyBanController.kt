package com.b1nd.dodamdodam.nightstudy.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.nightstudy.application.NightStudyBanUseCase
import com.b1nd.dodamdodam.nightstudy.application.data.request.CreateNightStudyBanRequest
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/ban")
class NightStudyBanController(
    private val nightStudyBanUseCase: NightStudyBanUseCase
) {

    @UserAccess(roles = [RoleType.TEACHER])
    @PostMapping
    fun create(@RequestBody request: CreateNightStudyBanRequest) =
        nightStudyBanUseCase.create(request)

    @UserAccess(hasAnyRoleOnly = true)
    @GetMapping
    fun getByUserId(@RequestParam userId: UUID) =
        nightStudyBanUseCase.getByUserId(userId)

    @UserAccess(roles = [RoleType.STUDENT])
    @GetMapping("/me")
    fun getMy() = nightStudyBanUseCase.getMy()

    @UserAccess(roles = [RoleType.TEACHER])
    @DeleteMapping
    fun deleteByUserId(@RequestParam userId: UUID) =
        nightStudyBanUseCase.deleteByUserId(userId)
}
