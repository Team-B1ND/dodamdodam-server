package com.b1nd.dodamdodam.nightstudy.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.nightstudy.application.NightStudyBanUseCase
import com.b1nd.dodamdodam.nightstudy.application.NightStudyProjectUseCase
import com.b1nd.dodamdodam.nightstudy.application.NightStudyUseCase
import com.b1nd.dodamdodam.nightstudy.application.data.request.ApplyNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.application.data.request.RejectNightStudyRequest
import org.springframework.web.bind.annotation.*

@RestController
class NightStudyController(
    private val nightStudyUseCase: NightStudyUseCase,
    private val nightStudyProjectUseCase: NightStudyProjectUseCase,
    private val nightStudyBanUseCase: NightStudyBanUseCase
) {

    @UserAccess(roles = [RoleType.STUDENT])
    @PostMapping
    fun apply(@RequestBody request: ApplyNightStudyRequest) =
        nightStudyUseCase.apply(request)

    @UserAccess(roles = [RoleType.STUDENT])
    @GetMapping("/me")
    fun getMy() = nightStudyUseCase.getMy()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping
    fun getAllowed() = nightStudyUseCase.getAllowed()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/all")
    fun getAll() = nightStudyUseCase.getAll()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/pending")
    fun getPending() = nightStudyUseCase.getPending()

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/{id}/allow")
    fun allow(@PathVariable id: Long) = nightStudyUseCase.allow(id)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/{id}/reject")
    fun reject(@PathVariable id: Long, @RequestBody request: RejectNightStudyRequest) =
        nightStudyUseCase.reject(id, request)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/{id}/revert")
    fun revert(@PathVariable id: Long) = nightStudyUseCase.revert(id)

    @UserAccess(roles = [RoleType.STUDENT])
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = nightStudyUseCase.delete(id)

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/projects")
    fun getValidProjects() = nightStudyProjectUseCase.getValid()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/bans")
    fun getAllBans() = nightStudyBanUseCase.getAll()

    @UserAccess(hasAnyRoleOnly = true)
    @GetMapping("/combined")
    fun getCombined() = nightStudyUseCase.getCombined()
}
