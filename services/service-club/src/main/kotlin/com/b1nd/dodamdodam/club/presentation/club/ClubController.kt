package com.b1nd.dodamdodam.club.presentation.club

import com.b1nd.dodamdodam.club.application.club.ClubApplicationUseCase
import com.b1nd.dodamdodam.club.application.club.ClubMemberUseCase
import com.b1nd.dodamdodam.club.application.club.ClubUseCase
import com.b1nd.dodamdodam.club.application.club.data.request.ClubPassRequest
import com.b1nd.dodamdodam.club.application.club.data.request.ClubTimeRequest
import com.b1nd.dodamdodam.club.application.club.data.request.CreateClubRequest
import com.b1nd.dodamdodam.club.application.club.data.request.JoinClubMemberRequest
import com.b1nd.dodamdodam.club.application.club.data.request.UpdateClubInfoRequest
import com.b1nd.dodamdodam.club.application.club.data.request.UpdateClubStatusRequest
import com.b1nd.dodamdodam.club.application.club.data.response.ClubDetailResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubJoinStudentResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubMemberResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubStatusResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubStudentListResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubStudentResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubTimeResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubWithLeaderResponse
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.infrastructure.grpc.MemberGrpcClient
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.core.security.util.getCurrentUserId
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ClubController(
    private val clubUseCase: ClubUseCase,
    private val clubMemberUseCase: ClubMemberUseCase,
    private val clubApplicationUseCase: ClubApplicationUseCase,
    private val memberGrpcClient: MemberGrpcClient,
) {
    private fun resolveStudentId(): Long {
        val userId = getCurrentUserId()
        val studentInfo = runBlocking {
            memberGrpcClient.getStudentByUserId(userId)
        } ?: throw IllegalStateException("학생 정보를 찾을 수 없어요")
        return studentInfo.id
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @PostMapping("/clubs/time")
    fun saveClubTime(@RequestBody request: ClubTimeRequest): Response<Unit> {
        clubUseCase.createTime(request)
        return Response.created("동아리 시간이 설정되었습니다.")
    }

    @UserAccess
    @PostMapping("/clubs")
    fun createClub(
        @RequestBody request: CreateClubRequest,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubUseCase.create(request, studentId)
        return Response.created("동아리가 생성되었습니다.")
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @PostMapping("/clubs/assignment")
    fun assignmentClubMembers(): Response<Unit> {
        clubApplicationUseCase.assignmentClubMembers()
        return Response.ok("동아리 배정이 완료되었습니다.")
    }

    @UserAccess
    @PostMapping("/clubs/status")
    fun setClubMemberStatus(
        @RequestBody request: ClubPassRequest,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubMemberUseCase.setClubMemberStatus(request, studentId)
        return Response.ok("동아리 멤버 상태가 변경되었습니다.")
    }

    @UserAccess
    @PostMapping("/clubs/join-requests")
    fun joinClubs(
        @RequestBody requests: List<JoinClubMemberRequest>,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubMemberUseCase.joinClubs(requests, studentId)
        return Response.created("동아리 가입 신청이 완료되었습니다.")
    }

    @UserAccess
    @PostMapping("/clubs/{id}/waiting")
    fun setWaiting(
        @PathVariable id: Long,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubUseCase.setWaiting(id, studentId)
        return Response.ok("동아리가 대기 상태로 변경되었습니다.")
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @PostMapping("/clubs/{id}/teacher")
    fun setTeacher(
        @PathVariable id: Long,
        @RequestBody teacherId: Long,
    ): Response<Unit> {
        clubUseCase.setTeacher(id, teacherId)
        return Response.ok("동아리 담당 선생님이 설정되었습니다.")
    }

    @UserAccess
    @PostMapping("/clubs/join-requests/{id}")
    fun acceptJoinRequest(
        @PathVariable id: Long,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubMemberUseCase.updateClubJoinRequestReceived(id, studentId, ClubStatus.ALLOWED)
        return Response.ok("동아리 가입 요청이 수락되었습니다.")
    }

    @UserAccess
    @DeleteMapping("/clubs/{id}")
    fun deleteClub(
        @PathVariable id: Long,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubUseCase.delete(id, studentId)
        return Response.ok("동아리가 삭제되었습니다.")
    }

    @UserAccess
    @DeleteMapping("/clubs/join-requests/{id}")
    fun rejectJoinRequest(
        @PathVariable id: Long,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubMemberUseCase.updateClubJoinRequestReceived(id, studentId, ClubStatus.REJECTED)
        return Response.ok("동아리 가입 요청이 거절되었습니다.")
    }

    @UserAccess
    @PatchMapping("/clubs/{id}")
    fun updateInfo(
        @PathVariable id: Long,
        @RequestBody request: UpdateClubInfoRequest,
    ): Response<Unit> {
        val studentId = resolveStudentId()
        clubUseCase.updateInfo(id, request, studentId)
        return Response.ok("동아리 정보가 수정되었습니다.")
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @PatchMapping("/clubs/state")
    fun updateState(@RequestBody request: UpdateClubStatusRequest): Response<Unit> {
        clubUseCase.update(request)
        return Response.ok("동아리 상태가 변경되었습니다.")
    }

    @UserAccess
    @GetMapping("/clubs/joined")
    fun getJoinedClubs(): Response<List<ClubMemberResponse>> {
        val studentId = resolveStudentId()
        return Response.ok("참여 동아리 조회 성공", clubMemberUseCase.getJoinedClubs(studentId))
    }

    @GetMapping("/clubs")
    fun getClubs(): Response<List<ClubDetailResponse>> =
        Response.ok("동아리 목록 조회 성공", clubUseCase.getClubs())

    @GetMapping("/clubs/leaders")
    fun getClubsWithLeader(): Response<List<ClubWithLeaderResponse>> =
        Response.ok("동아리 목록(리더 포함) 조회 성공", clubUseCase.getClubsWithLeader())

    @GetMapping("/clubs/{id}")
    fun getClubDetail(@PathVariable id: Long): Response<ClubDetailResponse> =
        Response.ok("동아리 상세 조회 성공", clubUseCase.getClubDetail(id))

    @UserAccess
    @GetMapping("/clubs/{clubId}/join-requests")
    fun getPendingClubMembers(@PathVariable clubId: Long): Response<List<ClubJoinStudentResponse>> =
        Response.ok("동아리 가입 요청 목록 조회 성공", clubMemberUseCase.getPendingClubMembers(clubId))

    @GetMapping("/clubs/{id}/leader")
    fun getClubLeader(@PathVariable id: Long): Response<ClubStudentResponse> =
        Response.ok("동아리 리더 조회 성공", clubMemberUseCase.getClubLeader(id))

    @GetMapping("/clubs/time")
    fun getClubTime(): Response<ClubTimeResponse> =
        Response.ok("동아리 시간 조회 성공", clubUseCase.find())

    @UserAccess
    @GetMapping("/clubs/join-requests/received")
    fun getReceivedJoinRequests(): Response<List<ClubMemberResponse>> {
        val studentId = resolveStudentId()
        return Response.ok("받은 동아리 가입 요청 조회 성공", clubMemberUseCase.getMemberJoinRequests(studentId))
    }

    @UserAccess
    @GetMapping("/clubs/{id}/members")
    fun getAllClubMembers(
        @PathVariable id: Long,
    ): Response<ClubStudentListResponse> {
        val studentId = resolveStudentId()
        return Response.ok("동아리 멤버 조회 성공", clubMemberUseCase.getAllClubMembers(id, studentId))
    }

    @UserAccess
    @GetMapping("/clubs/my")
    fun getMyClubStatus(): Response<List<ClubStatusResponse>> {
        val studentId = resolveStudentId()
        return Response.ok("내 동아리 상태 조회 성공", clubMemberUseCase.getStudentClubStatus(studentId))
    }

    @UserAccess
    @GetMapping("/clubs/my/join-requests")
    fun getMyJoinRequests(): Response<List<ClubMemberResponse>> {
        val studentId = resolveStudentId()
        return Response.ok("내 동아리 가입 요청 조회 성공", clubMemberUseCase.getStudentJoinRequest(studentId))
    }
}
