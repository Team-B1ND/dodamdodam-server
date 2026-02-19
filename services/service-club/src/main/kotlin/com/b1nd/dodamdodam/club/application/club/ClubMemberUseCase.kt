package com.b1nd.dodamdodam.club.application.club

import com.b1nd.dodamdodam.club.application.club.data.request.ClubPassRequest
import com.b1nd.dodamdodam.club.application.club.data.request.JoinClubMemberRequest
import com.b1nd.dodamdodam.club.application.club.data.response.ClubJoinStudentResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubMemberResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubStatusResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubStudentListResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubStudentResponse
import com.b1nd.dodamdodam.club.application.club.data.toClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubTimeType
import com.b1nd.dodamdodam.club.domain.club.service.ClubMemberService
import com.b1nd.dodamdodam.club.domain.club.service.ClubService
import com.b1nd.dodamdodam.club.infrastructure.grpc.MemberGrpcClient
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class ClubMemberUseCase(
    private val clubService: ClubService,
    private val clubMemberService: ClubMemberService,
    private val memberGrpcClient: MemberGrpcClient,
) {
    fun joinClubs(requests: List<JoinClubMemberRequest>, studentId: Long) {
        clubService.validateApplicationDuration(ClubTimeType.CLUB_APPLICANT)

        val members = requests.map { request ->
            val club = clubMemberService.findClubIfNotClubMember(
                clubId = request.clubId,
                state = ClubStatus.ALLOWED,
                studentId = studentId,
                excludeStatus = ClubStatus.DELETED,
            )
            request.toClubMemberEntity(studentId, club)
        }

        clubMemberService.saveAndValidateClubMembers(members)
    }

    fun setClubMemberStatus(request: ClubPassRequest, studentId: Long) {
        val club = clubService.findById(request.clubId)
        clubMemberService.validateByClubLeader(club, studentId)
        clubMemberService.setClubMemberStatus(request.clubId, request.studentId, request.status)
    }

    fun updateClubJoinRequestReceived(id: Long, studentId: Long, status: ClubStatus) {
        clubMemberService.setStatusStudentClub(studentId, id, status)
    }

    @Transactional(readOnly = true)
    fun getClubLeader(clubId: Long): ClubStudentResponse {
        val leader = clubMemberService.getClubLeader(clubId)
        val studentInfos = runBlocking {
            memberGrpcClient.getStudentsByIds(listOf(leader.studentId))
        }
        val studentInfo = studentInfos.firstOrNull()
        return ClubStudentResponse.fromEntity(
            clubMember = leader,
            name = studentInfo?.name ?: "",
            grade = studentInfo?.grade ?: 0,
            room = studentInfo?.room ?: 0,
            number = studentInfo?.number ?: 0,
            profileImage = studentInfo?.profileImage,
        )
    }

    @Transactional(readOnly = true)
    fun getPendingClubMembers(clubId: Long): List<ClubJoinStudentResponse> {
        val members = clubMemberService.getStatusClubMembers(clubId, ClubStatus.PENDING)
        val studentIds = members.map { it.studentId }
        val studentInfoMap = runBlocking {
            memberGrpcClient.getStudentsByIds(studentIds)
        }.associateBy { it.id }

        return members.map { member ->
            val studentInfo = studentInfoMap[member.studentId]
            ClubJoinStudentResponse.fromEntity(
                clubMember = member,
                name = studentInfo?.name ?: "",
                grade = studentInfo?.grade ?: 0,
                room = studentInfo?.room ?: 0,
                number = studentInfo?.number ?: 0,
                profileImage = studentInfo?.profileImage,
            )
        }
    }

    @Transactional(readOnly = true)
    fun getAllClubMembers(clubId: Long, studentId: Long): ClubStudentListResponse {
        val isLeader = clubMemberService.isClubLeader(clubId, studentId)
        val members = clubMemberService.getAllClubMembers(clubId)
        val studentIds = members.map { it.studentId }
        val studentInfoMap = runBlocking {
            memberGrpcClient.getStudentsByIds(studentIds)
        }.associateBy { it.id }

        val studentResponses = members.map { member ->
            val studentInfo = studentInfoMap[member.studentId]
            ClubStudentResponse.fromEntity(
                clubMember = member,
                name = studentInfo?.name ?: "",
                grade = studentInfo?.grade ?: 0,
                room = studentInfo?.room ?: 0,
                number = studentInfo?.number ?: 0,
                profileImage = studentInfo?.profileImage,
            )
        }

        return ClubStudentListResponse.of(isLeader, studentResponses)
    }

    @Transactional(readOnly = true)
    fun getMemberJoinRequests(studentId: Long): List<ClubMemberResponse> {
        val members = clubMemberService.findAllCreativeClubByStudent(studentId)
        return members.map { member ->
            val teacherName = member.club.teacherId?.let { teacherId ->
                runBlocking { memberGrpcClient.getTeacherById(teacherId) }?.name
            }
            ClubMemberResponse.fromEntity(member, teacherName)
        }
    }

    @Transactional(readOnly = true)
    fun getStudentJoinRequest(studentId: Long): List<ClubMemberResponse> {
        val members = clubMemberService.getJoinRequests(studentId, ClubStatus.PENDING)
        return members.map { member ->
            val teacherName = member.club.teacherId?.let { teacherId ->
                runBlocking { memberGrpcClient.getTeacherById(teacherId) }?.name
            }
            ClubMemberResponse.fromEntity(member, teacherName)
        }
    }

    @Transactional(readOnly = true)
    fun getJoinedClubs(studentId: Long): List<ClubMemberResponse> {
        val members = clubMemberService.getJoinRequests(studentId, ClubStatus.ALLOWED)
        return members.map { member ->
            val teacherName = member.club.teacherId?.let { teacherId ->
                runBlocking { memberGrpcClient.getTeacherById(teacherId) }?.name
            }
            ClubMemberResponse.fromEntity(member, teacherName)
        }
    }

    @Transactional(readOnly = true)
    fun getStudentClubStatus(studentId: Long): List<ClubStatusResponse> {
        val members = clubMemberService.getJoinRequests(studentId, ClubStatus.PENDING) +
            clubMemberService.getJoinRequests(studentId, ClubStatus.ALLOWED)
        return members.map { member ->
            ClubStatusResponse.fromEntity(member)
        }
    }
}
