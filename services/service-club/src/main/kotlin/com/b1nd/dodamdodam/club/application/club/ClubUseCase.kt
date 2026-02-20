package com.b1nd.dodamdodam.club.application.club

import com.b1nd.dodamdodam.club.application.club.data.request.ClubTimeRequest
import com.b1nd.dodamdodam.club.application.club.data.request.CreateClubRequest
import com.b1nd.dodamdodam.club.application.club.data.request.UpdateClubInfoRequest
import com.b1nd.dodamdodam.club.application.club.data.request.UpdateClubStatusRequest
import com.b1nd.dodamdodam.club.application.club.data.response.ClubDetailResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubStudentResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubTimeResponse
import com.b1nd.dodamdodam.club.application.club.data.response.ClubWithLeaderResponse
import com.b1nd.dodamdodam.club.application.club.data.toClubEntity
import com.b1nd.dodamdodam.club.application.club.data.toClubTimeEntity
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
class ClubUseCase(
    private val clubService: ClubService,
    private val clubMemberService: ClubMemberService,
    private val memberGrpcClient: MemberGrpcClient,
) {
    fun createTime(request: ClubTimeRequest) {
        clubService.setClubTime(request.toClubTimeEntity())
    }

    fun create(request: CreateClubRequest, leaderStudentId: Long) {
        clubService.validateApplicationDuration(ClubTimeType.CLUB_CREATED)
        clubService.checkIsNameDuplicated(request.name)
        val club = request.toClubEntity()
        clubMemberService.validateAndRejectLeader(club, leaderStudentId, request.studentIds)
        clubService.createClubAndMembers(club, leaderStudentId, request.studentIds)
    }

    fun delete(id: Long, studentId: Long) {
        val club = clubService.findById(id)
        clubMemberService.validateByClubLeader(club, studentId)
        clubMemberService.setDeleteAllClubMembers(club)
        clubService.deleteClub(club)
    }

    fun setWaiting(id: Long, studentId: Long) {
        val club = clubService.findById(id)
        clubMemberService.validateActiveClubMemberSize(club, studentId)
        clubMemberService.setDeleteClubMembers(club)
        club.updateStatus(ClubStatus.PENDING, null)
    }

    fun setTeacher(clubId: Long, teacherId: Long) {
        val club = clubService.findById(clubId)
        club.assignTeacher(teacherId)
    }

    fun update(request: UpdateClubStatusRequest) {
        val clubs = clubService.findByIds(request.clubIds)
        clubs.forEach { club ->
            club.updateStatus(request.status, request.reason)
        }
        clubService.createAll(clubs)
    }

    fun updateInfo(id: Long, request: UpdateClubInfoRequest, studentId: Long) {
        val club = clubService.findById(id)
        clubMemberService.validateByClubLeader(club, studentId)
        clubService.checkIsNameDuplicated(request.name)
        club.updateInfo(
            name = request.name,
            subject = request.subject,
            shortDescription = request.shortDescription,
            description = request.description,
            image = request.image,
        )
    }

    @Transactional(readOnly = true)
    fun find(): ClubTimeResponse {
        val createTime = clubService.getClubTime(ClubTimeType.CLUB_CREATED)
        val applicantTime = clubService.getClubTime(ClubTimeType.CLUB_APPLICANT)
        return ClubTimeResponse.fromEntities(createTime, applicantTime)
    }

    @Transactional(readOnly = true)
    fun getClubs(): List<ClubDetailResponse> {
        val clubs = clubService.findAll()
        val teacherIds = clubs.mapNotNull { it.teacherId }.distinct()
        val teacherMap = runBlocking {
            teacherIds.associateWith { teacherId ->
                memberGrpcClient.getTeacherById(teacherId)?.name
            }
        }
        return clubs.map { club ->
            ClubDetailResponse.fromEntity(club, club.teacherId?.let { teacherMap[it] })
        }
    }

    @Transactional(readOnly = true)
    fun getClubsWithLeader(): List<ClubWithLeaderResponse> {
        val clubs = clubService.findAll()
        val leaders = clubMemberService.getAllLeaders()
        val leaderMap = leaders.groupBy { it.club.id!! }

        val studentIds = leaders.map { it.studentId }.distinct()
        val teacherIds = clubs.mapNotNull { it.teacherId }.distinct()

        val studentInfoMap = runBlocking {
            memberGrpcClient.getStudentsByIds(studentIds).associateBy { it.id }
        }
        val teacherMap = runBlocking {
            teacherIds.associateWith { teacherId ->
                memberGrpcClient.getTeacherById(teacherId)?.name
            }
        }

        return clubs.map { club ->
            val leaderMember = leaderMap[club.id]?.firstOrNull()
            val leaderResponse = leaderMember?.let { member ->
                val studentInfo = studentInfoMap[member.studentId]
                studentInfo?.let {
                    ClubStudentResponse.fromEntity(
                        clubMember = member,
                        name = it.name,
                        grade = it.grade,
                        room = it.room,
                        number = it.number,
                        profileImage = it.profileImage,
                    )
                }
            }
            ClubWithLeaderResponse.fromEntity(
                club = club,
                leader = leaderResponse,
                teacherName = club.teacherId?.let { teacherMap[it] },
            )
        }
    }

    @Transactional(readOnly = true)
    fun getClubDetail(id: Long): ClubDetailResponse {
        val club = clubService.findById(id)
        val teacherName = club.teacherId?.let { teacherId ->
            runBlocking { memberGrpcClient.getTeacherById(teacherId)?.name }
        }
        return ClubDetailResponse.fromEntity(club, teacherName)
    }
}
