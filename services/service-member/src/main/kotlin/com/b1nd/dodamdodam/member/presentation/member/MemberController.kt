package com.b1nd.dodamdodam.member.presentation.member

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.member.application.member.MemberUseCase
import com.b1nd.dodamdodam.member.application.member.data.AuthType
import com.b1nd.dodamdodam.member.application.member.data.request.ApplyBroadcastClubMemberRequest
import com.b1nd.dodamdodam.member.application.member.data.request.ApplyDormitoryManageMemberRequest
import com.b1nd.dodamdodam.member.application.member.data.request.AuthCodeRequest
import com.b1nd.dodamdodam.member.application.member.data.request.JoinStudentRequest
import com.b1nd.dodamdodam.member.application.member.data.request.JoinTeacherRequest
import com.b1nd.dodamdodam.member.application.member.data.request.UpdateMemberInfoRequest
import com.b1nd.dodamdodam.member.application.member.data.request.UpdateStudentInfoRequest
import com.b1nd.dodamdodam.member.application.member.data.request.UpdateTeacherForAdminRequest
import com.b1nd.dodamdodam.member.application.member.data.request.VerifyAuthCodeRequest
import com.b1nd.dodamdodam.member.application.member.data.response.MemberInfoResponse
import com.b1nd.dodamdodam.member.domain.member.enumeration.ActiveStatus
import org.springframework.security.core.context.SecurityContextHolder
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
@RequestMapping("/member")
class MemberController(
    private val memberUseCase: MemberUseCase,
) {
    private fun getCurrentMemberId(): Long {
        val principal = SecurityContextHolder.getContext().authentication.principal as PassportUserDetails
        return memberUseCase.getMemberIdByUsername(principal.username)
    }

    @PostMapping("/join-student")
    fun joinStudent(@RequestBody request: JoinStudentRequest): Response<Unit> {
        memberUseCase.joinStudent(request)
        return Response.created("SUCCESS")
    }

    @PostMapping("/join-teacher")
    fun joinTeacher(@RequestBody request: JoinTeacherRequest): Response<Unit> {
        memberUseCase.joinTeacher(request)
        return Response.created("SUCCESS")
    }

    // TODO: Implement auth code sending via external email/SMS service
    @PostMapping("/auth-code/{type}")
    fun sendAuthCode(
        @PathVariable type: AuthType,
        @RequestBody request: AuthCodeRequest,
    ): Response<Unit> = Response.ok("SUCCESS")

    // TODO: Implement auth code verification
    @PostMapping("/auth-code/{type}/verify")
    fun verifyAuthCode(
        @PathVariable type: AuthType,
        @RequestBody request: VerifyAuthCodeRequest,
    ): Response<Unit> = Response.ok("SUCCESS")

    @PostMapping("/broadcast-club-member")
    fun applyBroadcastClubMember(@RequestBody request: ApplyBroadcastClubMemberRequest): Response<Unit> {
        memberUseCase.applyBroadcastClubMember(request)
        return Response.created("SUCCESS")
    }

    @DeleteMapping("/broadcast-club-member")
    fun removeBroadcastClubMember(@RequestBody request: ApplyBroadcastClubMemberRequest): Response<Unit> {
        memberUseCase.removeBroadcastClubMember(request)
        return Response.ok("SUCCESS")
    }

    @GetMapping("/check/broadcast-club-member")
    fun checkMyBroadcastClubMember(): Response<Boolean> {
        val memberId = getCurrentMemberId()
        return Response.ok("SUCCESS", memberUseCase.checkBroadcastClubMember(memberId))
    }

    @GetMapping("/check/broadcast-club-member/{id}")
    fun checkBroadcastClubMember(@PathVariable id: Long): Response<Boolean> =
        Response.ok("SUCCESS", memberUseCase.checkBroadcastClubMember(id))

    @PostMapping("/dormitory-manage-member")
    fun applyDormitoryManageMember(@RequestBody request: ApplyDormitoryManageMemberRequest): Response<Unit> {
        memberUseCase.applyDormitoryManageMember(request)
        return Response.created("SUCCESS")
    }

    @DeleteMapping("/dormitory-manage-member")
    fun removeDormitoryManageMember(@RequestBody request: ApplyDormitoryManageMemberRequest): Response<Unit> {
        memberUseCase.removeDormitoryManageMember(request)
        return Response.ok("SUCCESS")
    }

    @GetMapping("/check/dormitory-manage-member")
    fun checkMyDormitoryManageMember(): Response<Boolean> {
        val memberId = getCurrentMemberId()
        return Response.ok("SUCCESS", memberUseCase.checkDormitoryManageMember(memberId))
    }

    @GetMapping("/check/dormitory-manage-member/{id}")
    fun checkDormitoryManageMember(@PathVariable id: Long): Response<Boolean> =
        Response.ok("SUCCESS", memberUseCase.checkDormitoryManageMember(id))

    @GetMapping("/{id}")
    fun getMemberInfo(@PathVariable id: Long): Response<MemberInfoResponse> =
        Response.ok("SUCCESS", memberUseCase.getMemberInfo(id))

    @GetMapping("/my")
    fun getMyInfo(): Response<MemberInfoResponse> {
        val memberId = getCurrentMemberId()
        return Response.ok("SUCCESS", memberUseCase.getMyInfo(memberId))
    }

    @GetMapping("/search")
    fun searchMembers(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) grade: Int?,
        @RequestParam(required = false) role: RoleType?,
        @RequestParam(required = false) status: ActiveStatus?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int,
    ): Response<List<MemberInfoResponse>> =
        Response.ok("SUCCESS", memberUseCase.searchMembers(name, grade, role, status, page, pageSize))

    @GetMapping("/search/info")
    fun searchMembersWithInfo(
        @RequestParam name: String,
        @RequestParam role: RoleType,
        @RequestParam status: ActiveStatus,
        @RequestParam(required = false) grade: Int?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int,
    ): Response<List<MemberInfoResponse>> =
        Response.ok("SUCCESS", memberUseCase.searchMembersWithInfo(name, role, status, grade, page, pageSize))

    @GetMapping("/status")
    fun getMembersByStatus(@RequestParam status: ActiveStatus): Response<List<MemberInfoResponse>> =
        Response.ok("SUCCESS", memberUseCase.getMembersByStatus(status))

    @GetMapping("/all")
    fun getAllActiveMembers(): Response<List<MemberInfoResponse>> =
        Response.ok("SUCCESS", memberUseCase.getAllActiveMembers())

    @PatchMapping("/status/{id}")
    fun updateMemberStatus(
        @PathVariable id: Long,
        @RequestParam status: ActiveStatus,
    ): Response<Unit> {
        memberUseCase.updateMemberStatus(id, status)
        return Response.ok("SUCCESS")
    }

    @DeleteMapping("/{id}")
    fun deleteMember(@PathVariable id: Long): Response<Unit> {
        memberUseCase.deleteMember(id)
        return Response.ok("SUCCESS")
    }

    @PatchMapping("/info")
    fun updateMemberInfo(@RequestBody request: UpdateMemberInfoRequest): Response<Unit> {
        val memberId = getCurrentMemberId()
        memberUseCase.updateMemberInfo(memberId, request)
        return Response.ok("SUCCESS")
    }

    @PatchMapping("/student/info")
    fun updateStudentInfo(@RequestBody request: UpdateStudentInfoRequest): Response<Unit> {
        val memberId = getCurrentMemberId()
        memberUseCase.updateStudentInfo(memberId, request)
        return Response.ok("SUCCESS")
    }

    @PatchMapping("/teacher/info/{id}")
    fun updateTeacherInfoForAdmin(
        @PathVariable id: Long,
        @RequestBody request: UpdateTeacherForAdminRequest,
    ): Response<Unit> {
        memberUseCase.updateTeacherInfoForAdmin(id, request)
        return Response.ok("SUCCESS")
    }

    @PatchMapping("/deactivate")
    fun deactivateSelf(): Response<Unit> {
        val memberId = getCurrentMemberId()
        memberUseCase.deactivateSelf(memberId)
        return Response.ok("SUCCESS")
    }

    @PatchMapping("/deactivate-graduate")
    fun deactivateGraduates(): Response<Unit> {
        memberUseCase.deactivateGraduates()
        return Response.ok("SUCCESS")
    }

    @GetMapping("/health")
    fun health(): Response<Unit> = Response.ok("OK")
}
