package com.b1nd.dodamdodam.outsleeping.presentation.outsleeping

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.OutSleepingUseCase
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.ApplyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.ModifyOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.RejectOutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.UpdateDeadlineRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class OutSleepingController(
    private val outSleepingUseCase: OutSleepingUseCase,
) {

    @UserAccess
    @PostMapping("/out-sleeping")
    fun apply(@RequestBody request: ApplyOutSleepingRequest) =
        outSleepingUseCase.apply(request)

    @UserAccess(roles = [RoleType.STUDENT])
    @PatchMapping("/out-sleeping/{id}")
    fun modify(@PathVariable id: Long, @RequestBody request: ModifyOutSleepingRequest) =
        outSleepingUseCase.modify(id, request)

    @UserAccess(roles = [RoleType.STUDENT])
    @DeleteMapping("/out-sleeping/{id}")
    fun cancel(@PathVariable id: Long) =
        outSleepingUseCase.cancel(id)

    @UserAccess(roles = [RoleType.STUDENT])
    @GetMapping("/out-sleeping/my")
    fun getMy() =
        outSleepingUseCase.getMy()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/out-sleeping")
    fun getByDate(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ) = outSleepingUseCase.getByDate(date, page, size)

    @UserAccess
    @GetMapping("/out-sleeping/valid")
    fun getValid() =
        outSleepingUseCase.getValid()

    @UserAccess(roles = [RoleType.TEACHER])
    @GetMapping("/out-sleeping/residual")
    fun getResidual() =
        outSleepingUseCase.getResidual()

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/out-sleeping/{id}/allow")
    fun allow(@PathVariable id: Long) =
        outSleepingUseCase.allow(id)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/out-sleeping/{id}/reject")
    fun reject(@PathVariable id: Long, @RequestBody request: RejectOutSleepingRequest) =
        outSleepingUseCase.reject(id, request)

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/out-sleeping/{id}/revert")
    fun revert(@PathVariable id: Long) =
        outSleepingUseCase.revert(id)

    @UserAccess
    @GetMapping("/out-sleeping/deadline")
    fun getDeadline() =
        outSleepingUseCase.getDeadline()

    @UserAccess(roles = [RoleType.TEACHER])
    @PatchMapping("/out-sleeping/deadline")
    fun updateDeadline(@RequestBody request: UpdateDeadlineRequest) =
        outSleepingUseCase.updateDeadline(request)
}
