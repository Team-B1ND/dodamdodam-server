package com.b1nd.dodamdodam.outsleeping.presentation.outsleeping

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.OutSleepingUseCase
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.OutSleepingRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request.RejectRequest
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.OutSleepingResponse
import com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response.ResidualStudentResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/out-sleeping")
class OutSleepingController(
    private val useCase: OutSleepingUseCase
) {
    @PostMapping
    @UserAccess(roles = [RoleType.STUDENT])
    fun apply(
        @RequestBody request: OutSleepingRequest
    ): ResponseEntity<Response<Unit>> {
        useCase.apply(request)
        return Response.created<Unit>("외박 신청이 완료되었어요.").toResponseEntity()
    }

    @PatchMapping("/{id}/allow")
    @UserAccess(roles = [RoleType.TEACHER])
    fun allow(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.allow(id)
        return Response.noContent<Unit>("외박이 승인되었어요.").toResponseEntity()
    }

    @PatchMapping("/{id}/reject")
    @UserAccess(roles = [RoleType.TEACHER])
    fun reject(
        @PathVariable id: Long,
        @RequestBody request: RejectRequest
    ): ResponseEntity<Response<Unit>> {
        useCase.reject(id, request)
        return Response.noContent<Unit>("외박이 거절되었어요.").toResponseEntity()
    }

    @PatchMapping("/{id}/revert")
    @UserAccess(roles = [RoleType.TEACHER])
    fun revert(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.revert(id)
        return Response.noContent<Unit>("외박 승인이 취소되었어요.").toResponseEntity()
    }

    @DeleteMapping("/{id}")
    @UserAccess(roles = [RoleType.STUDENT])
    fun delete(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.delete(id)
        return Response.noContent<Unit>("외박 신청이 취소되었어요.").toResponseEntity()
    }

    @GetMapping
    @UserAccess(roles = [RoleType.TEACHER])
    fun findByDate(
        @RequestParam date: LocalDate
    ): ResponseEntity<Response<List<OutSleepingResponse>>> {
        val data = useCase.findByDate(date)
        return Response.ok("날짜별 외박 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/my")
    @UserAccess(roles = [RoleType.STUDENT])
    fun findMy(): ResponseEntity<Response<List<OutSleepingResponse>>> {
        val data = useCase.findMy()
        return Response.ok("내 외박 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/valid")
    @UserAccess(roles = [RoleType.TEACHER])
    fun findValid(): ResponseEntity<Response<List<OutSleepingResponse>>> {
        val data = useCase.findValid()
        return Response.ok("유효한 외박 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/residual")
    @UserAccess(roles = [RoleType.TEACHER])
    fun findResidualStudents(): ResponseEntity<Response<List<ResidualStudentResponse>>> {
        val data = useCase.findResidualStudents()
        return Response.ok("잔류 학생 조회 성공", data).toResponseEntity()
    }
}
