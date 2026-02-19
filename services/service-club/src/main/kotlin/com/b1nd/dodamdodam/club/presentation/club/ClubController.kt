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
import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clubs")
class ClubController(
    private val clubUseCase: ClubUseCase,
    private val clubMemberUseCase: ClubMemberUseCase,
    private val clubApplicationUseCase: ClubApplicationUseCase,
    private val memberGrpcClient: MemberGrpcClient,
) {
    private fun resolveStudentId(userDetails: PassportUserDetails): Long {
        val username = userDetails.username
        val studentInfo = runBlocking {
            memberGrpcClient.getStudentByUsername(username)
        } ?: throw IllegalStateException("Student not found for username: $username")
        return studentInfo.id
    }

    @PostMapping("/time")
    fun saveClubTime(@RequestBody request: ClubTimeRequest): Response<Unit> {
        clubUseCase.save(request)
        return Response.created("동아리 시간이 설정되었습니다.")
    }

    @PostMapping
    fun createClub(
        @RequestBody request: CreateClubRequest,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubUseCase.save(request, studentId)
        return Response.created("동아리가 생성되었습니다.")
    }

    @PostMapping("/assignment")
    fun assignmentClubMembers(): Response<Unit> {
        clubApplicationUseCase.assignmentClubMembers()
        return Response.ok("동아리 배정이 완료되었습니다.")
    }

    @PostMapping("/status")
    fun setClubMemberStatus(
        @RequestBody request: ClubPassRequest,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubMemberUseCase.setClubMemberStatus(request, studentId)
        return Response.ok("동아리 멤버 상태가 변경되었습니다.")
    }

    @PostMapping("/join-requests")
    fun joinClubs(
        @RequestBody requests: List<JoinClubMemberRequest>,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubMemberUseCase.joinClubs(requests, studentId)
        return Response.created("동아리 가입 신청이 완료되었습니다.")
    }

    @PostMapping("/{id}/waiting")
    fun setWaiting(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubUseCase.setWaiting(id, studentId)
        return Response.ok("동아리가 대기 상태로 변경되었습니다.")
    }

    @PostMapping("/{id}/teacher")
    fun setTeacher(
        @PathVariable id: Long,
        @RequestBody teacherId: Long,
    ): Response<Unit> {
        clubUseCase.setTeacher(id, teacherId)
        return Response.ok("동아리 담당 선생님이 설정되었습니다.")
    }

    @PostMapping("/join-requests/{id}")
    fun acceptJoinRequest(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubMemberUseCase.updateClubJoinRequestReceived(id, studentId, ClubStatus.ALLOWED)
        return Response.ok("동아리 가입 요청이 수락되었습니다.")
    }

    @DeleteMapping("/{id}")
    fun deleteClub(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubUseCase.delete(id, studentId)
        return Response.ok("동아리가 삭제되었습니다.")
    }

    @DeleteMapping("/join-requests/{id}")
    fun rejectJoinRequest(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubMemberUseCase.updateClubJoinRequestReceived(id, studentId, ClubStatus.REJECTED)
        return Response.ok("동아리 가입 요청이 거절되었습니다.")
    }

    @PatchMapping("/{id}")
    fun updateInfo(
        @PathVariable id: Long,
        @RequestBody request: UpdateClubInfoRequest,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<Unit> {
        val studentId = resolveStudentId(userDetails)
        clubUseCase.updateInfo(id, request, studentId)
        return Response.ok("동아리 정보가 수정되었습니다.")
    }

    @PatchMapping("/state")
    fun updateState(@RequestBody request: UpdateClubStatusRequest): Response<Unit> {
        clubUseCase.update(request)
        return Response.ok("동아리 상태가 변경되었습니다.")
    }

    @GetMapping("/joined")
    fun getJoinedClubs(
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<List<ClubMemberResponse>> {
        val studentId = resolveStudentId(userDetails)
        return Response.ok("참여 동아리 조회 성공", clubMemberUseCase.getJoinedClubs(studentId))
    }

    @GetMapping
    fun getClubs(): Response<List<ClubDetailResponse>> =
        Response.ok("동아리 목록 조회 성공", clubUseCase.getClubs())

    @GetMapping("/leaders")
    fun getClubsWithLeader(): Response<List<ClubWithLeaderResponse>> =
        Response.ok("동아리 목록(리더 포함) 조회 성공", clubUseCase.getClubsWithLeader())

    @GetMapping("/{id}")
    fun getClubDetail(@PathVariable id: Long): Response<ClubDetailResponse> =
        Response.ok("동아리 상세 조회 성공", clubUseCase.getClubDetail(id))

    @GetMapping("/{clubId}/join-requests")
    fun getPendingClubMembers(@PathVariable clubId: Long): Response<List<ClubJoinStudentResponse>> =
        Response.ok("동아리 가입 요청 목록 조회 성공", clubMemberUseCase.getPendingClubMembers(clubId))

    @GetMapping("/{id}/leader")
    fun getClubLeader(@PathVariable id: Long): Response<ClubStudentResponse> =
        Response.ok("동아리 리더 조회 성공", clubMemberUseCase.getClubLeader(id))

    @GetMapping("/time")
    fun getClubTime(): Response<ClubTimeResponse> =
        Response.ok("동아리 시간 조회 성공", clubUseCase.find())

    @GetMapping("/join-requests/received")
    fun getReceivedJoinRequests(
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<List<ClubMemberResponse>> {
        val studentId = resolveStudentId(userDetails)
        return Response.ok("받은 동아리 가입 요청 조회 성공", clubMemberUseCase.getMemberJoinRequests(studentId))
    }

    @GetMapping("/{id}/members")
    fun getAllClubMembers(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<ClubStudentListResponse> {
        val studentId = resolveStudentId(userDetails)
        return Response.ok("동아리 멤버 조회 성공", clubMemberUseCase.getAllClubMembers(id, studentId))
    }

    @GetMapping("/my")
    fun getMyClubStatus(
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<List<ClubStatusResponse>> {
        val studentId = resolveStudentId(userDetails)
        return Response.ok("내 동아리 상태 조회 성공", clubMemberUseCase.getStudentClubStatus(studentId))
    }

    @GetMapping("/my/join-requests")
    fun getMyJoinRequests(
        @AuthenticationPrincipal userDetails: PassportUserDetails,
    ): Response<List<ClubMemberResponse>> {
        val studentId = resolveStudentId(userDetails)
        return Response.ok("내 동아리 가입 요청 조회 성공", clubMemberUseCase.getStudentJoinRequest(studentId))
    }

    @GetMapping("/health")
    fun health(): String = "OK"
}
