package com.b1nd.dodamdodam.nightstudy.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.nightstudy.application.NightStudyProjectUseCase
import com.b1nd.dodamdodam.nightstudy.application.data.request.ApplyNightStudyProjectRequest
import com.b1nd.dodamdodam.nightstudy.application.data.request.RejectNightStudyRequest
import com.b1nd.dodamdodam.nightstudy.domain.project.enumeration.ProjectRoom
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project")
class NightStudyProjectController(
    private val nightStudyProjectUseCase: NightStudyProjectUseCase
) {

    @UserAccess(roles = [RoleType.STUDENT])
    @PostMapping
    fun apply(@RequestBody request: ApplyNightStudyProjectRequest) =
        nightStudyProjectUseCase.apply(request)

    @UserAccess(roles = [RoleType.STUDENT])
    @GetMapping("/me")
    fun getMy() = nightStudyProjectUseCase.getMy()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/pending")
    fun getPending() = nightStudyProjectUseCase.getPending()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/allowed")
    fun getAllowed() = nightStudyProjectUseCase.getAllowed()

    @UserAccess(hasAnyRoleOnly = true)
    @GetMapping("/rooms")
    fun getRooms() = nightStudyProjectUseCase.getRooms()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/students")
    fun getStudents() = nightStudyProjectUseCase.getStudents()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = nightStudyProjectUseCase.getById(id)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/{id}/allow/{room}")
    fun allow(@PathVariable id: Long, @PathVariable room: ProjectRoom) =
        nightStudyProjectUseCase.allow(id, room)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/{id}/reject")
    fun reject(@PathVariable id: Long, @RequestBody request: RejectNightStudyRequest) =
        nightStudyProjectUseCase.reject(id, request)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/{id}/revert")
    fun revert(@PathVariable id: Long) = nightStudyProjectUseCase.revert(id)

    @UserAccess(roles = [RoleType.STUDENT])
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = nightStudyProjectUseCase.delete(id)
}
