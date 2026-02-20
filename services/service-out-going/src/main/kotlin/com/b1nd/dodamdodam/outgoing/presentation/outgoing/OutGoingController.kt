package com.b1nd.dodamdodam.outgoing.presentation.outgoing

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.outgoing.application.outgoing.OutGoingUseCase
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.request.OutGoingRequest
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.request.RejectRequest
import com.b1nd.dodamdodam.outgoing.application.outgoing.data.response.OutGoingResponse
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
@RequestMapping("/out-going")
class OutGoingController(
    private val useCase: OutGoingUseCase
) {
    @PostMapping
    @UserAccess(roles = [RoleType.STUDENT])
    fun apply(
        @RequestBody request: OutGoingRequest
    ): ResponseEntity<Response<Unit>> {
        useCase.apply(request)
        return Response.created<Unit>("외출 신청이 완료되었어요.").toResponseEntity()
    }

    @PatchMapping("/{id}/allow")
    @UserAccess(roles = [RoleType.TEACHER])
    fun allow(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.allow(id)
        return Response.noContent<Unit>("외출이 승인되었어요.").toResponseEntity()
    }

    @PatchMapping("/{id}/reject")
    @UserAccess(roles = [RoleType.TEACHER])
    fun reject(
        @PathVariable id: Long,
        @RequestBody request: RejectRequest
    ): ResponseEntity<Response<Unit>> {
        useCase.reject(id, request)
        return Response.noContent<Unit>("외출이 거절되었어요.").toResponseEntity()
    }

    @PatchMapping("/{id}/revert")
    @UserAccess(roles = [RoleType.TEACHER])
    fun revert(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.revert(id)
        return Response.noContent<Unit>("외출 승인이 취소되었어요.").toResponseEntity()
    }

    @DeleteMapping("/{id}")
    @UserAccess(roles = [RoleType.STUDENT])
    fun delete(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.delete(id)
        return Response.noContent<Unit>("외출 신청이 취소되었어요.").toResponseEntity()
    }

    @GetMapping
    @UserAccess(roles = [RoleType.TEACHER])
    fun findByDate(
        @RequestParam date: LocalDate
    ): ResponseEntity<Response<List<OutGoingResponse>>> {
        val data = useCase.findByDate(date)
        return Response.ok("날짜별 외출 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/my")
    @UserAccess(roles = [RoleType.STUDENT])
    fun findMy(): ResponseEntity<Response<List<OutGoingResponse>>> {
        val data = useCase.findMy()
        return Response.ok("내 외출 조회 성공", data).toResponseEntity()
    }
}
