package com.b1nd.dodamdodam.wakeupsong.presentation.wakeupsong

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.WakeupSongUseCase
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.request.WakeupSongKeywordRequest
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.request.WakeupSongUrlRequest
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.MelonChartResponse
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.WakeupSongResponse
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.WakeupSongSearchResponse
import jakarta.validation.Valid
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

@RestController
@RequestMapping("/wakeup-song")
class WakeupSongController(
    private val useCase: WakeupSongUseCase
) {

    @GetMapping("/my")
    @UserAccess(roles = [RoleType.STUDENT])
    fun getMy(): ResponseEntity<Response<List<WakeupSongResponse>>> {
        val data = useCase.getMy()
        return Response.ok("내 기상송 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/allowed")
    @UserAccess(roles = [RoleType.STUDENT])
    fun getAllowed(): ResponseEntity<Response<List<WakeupSongResponse>>> {
        val data = useCase.getAllowed()
        return Response.ok("승인된 기상송 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/pending")
    @UserAccess(roles = [RoleType.TEACHER])
    fun getPending(): ResponseEntity<Response<List<WakeupSongResponse>>> {
        val data = useCase.getPending()
        return Response.ok("대기 중인 기상송 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/chart")
    @UserAccess(roles = [RoleType.STUDENT])
    fun getChart(): ResponseEntity<Response<List<MelonChartResponse>>> {
        val data = useCase.getChart()
        return Response.ok("멜론 차트 조회 성공", data).toResponseEntity()
    }

    @GetMapping("/search")
    @UserAccess(roles = [RoleType.STUDENT])
    fun search(
        @RequestParam keyword: String
    ): ResponseEntity<Response<List<WakeupSongSearchResponse>>> {
        val data = useCase.search(keyword)
        return Response.ok("유튜브 검색 성공", data).toResponseEntity()
    }

    @PostMapping
    @UserAccess(roles = [RoleType.STUDENT])
    fun applyByUrl(
        @RequestBody @Valid request: WakeupSongUrlRequest
    ): ResponseEntity<Response<Unit>> {
        useCase.applyByUrl(request)
        return Response.created<Unit>("기상송 신청이 완료되었어요.").toResponseEntity()
    }

    @PostMapping("/keyword")
    @UserAccess(roles = [RoleType.STUDENT])
    fun applyByKeyword(
        @RequestBody @Valid request: WakeupSongKeywordRequest
    ): ResponseEntity<Response<Unit>> {
        useCase.applyByKeyword(request)
        return Response.created<Unit>("키워드로 기상송 신청이 완료되었어요.").toResponseEntity()
    }

    @PatchMapping("/allow/{id}")
    @UserAccess(roles = [RoleType.TEACHER])
    fun allow(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.allow(id)
        return Response.noContent<Unit>("기상송이 승인되었어요.").toResponseEntity()
    }

    @PatchMapping("/deny/{id}")
    @UserAccess(roles = [RoleType.TEACHER])
    fun deny(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.deny(id)
        return Response.noContent<Unit>("기상송이 거절되었어요.").toResponseEntity()
    }

    @DeleteMapping("/my/{id}")
    @UserAccess(roles = [RoleType.STUDENT])
    fun deleteMy(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.deleteMy(id)
        return Response.noContent<Unit>("기상송 신청이 취소되었어요.").toResponseEntity()
    }

    @DeleteMapping("/{id}")
    @UserAccess(roles = [RoleType.TEACHER])
    fun deleteById(@PathVariable id: Long): ResponseEntity<Response<Unit>> {
        useCase.deleteById(id)
        return Response.noContent<Unit>("기상송이 삭제되었어요.").toResponseEntity()
    }
}
