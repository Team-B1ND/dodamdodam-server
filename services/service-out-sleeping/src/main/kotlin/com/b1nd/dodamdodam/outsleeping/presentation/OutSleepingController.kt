package com.b1nd.dodamdodam.outsleeping.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.outsleeping.application.OutSleepingUseCase
import com.b1nd.dodamdodam.outsleeping.application.data.request.ApplyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.data.request.RejectOutSleepingRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
class OutSleepingController(
    private val outSleepingUseCase: OutSleepingUseCase
) {

    @UserAccess(hasAnyRoleOnly = true)
    @PostMapping
    fun apply(@Valid @RequestBody request: ApplyOutSleepingRequest) =
        outSleepingUseCase.apply(request)

    @UserAccess(roles = [RoleType.DORMITORY_MANAGER])
    @PatchMapping("/{id}/allow")
    fun allow(@PathVariable id: Long) =
        outSleepingUseCase.allow(id)

    @UserAccess(roles = [RoleType.DORMITORY_MANAGER])
    @PatchMapping("/{id}/reject")
    fun reject(@PathVariable id: Long, @RequestBody request: RejectOutSleepingRequest) =
        outSleepingUseCase.reject(id, request)

    @UserAccess(roles = [RoleType.DORMITORY_MANAGER])
    @PatchMapping("/{id}/revert")
    fun revert(@PathVariable id: Long) =
        outSleepingUseCase.revert(id)

    @UserAccess(roles = [RoleType.STUDENT])
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) =
        outSleepingUseCase.delete(id)

    @UserAccess(roles = [RoleType.DORMITORY_MANAGER])
    @GetMapping
    fun getByDate(@RequestParam date: LocalDate) =
        outSleepingUseCase.getByDate(date)

    @UserAccess(roles = [RoleType.STUDENT])
    @GetMapping("/me")
    fun getMy() =
        outSleepingUseCase.getMy()

    @UserAccess(roles = [RoleType.DORMITORY_MANAGER])
    @GetMapping("/residual")
    fun getResidual(@RequestParam date: LocalDate) =
        outSleepingUseCase.getResidual(date)
}
