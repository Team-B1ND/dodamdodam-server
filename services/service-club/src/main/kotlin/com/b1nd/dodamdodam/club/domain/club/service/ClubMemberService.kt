package com.b1nd.dodamdodam.club.domain.club.service

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPermission
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPriority
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import com.b1nd.dodamdodam.club.domain.club.exception.ClubJoinedException
import com.b1nd.dodamdodam.club.domain.club.exception.ClubMemberNotFoundException
import com.b1nd.dodamdodam.club.domain.club.exception.ClubNotFoundException
import com.b1nd.dodamdodam.club.domain.club.exception.ClubPermissionDeniedException
import com.b1nd.dodamdodam.club.domain.club.exception.InsufficientClubMembersException
import com.b1nd.dodamdodam.club.domain.club.exception.InvalidClubMemberInviteException
import com.b1nd.dodamdodam.club.domain.club.repository.ClubMemberRepository
import com.b1nd.dodamdodam.club.domain.club.repository.ClubRepository
import org.springframework.stereotype.Service

@Service
class ClubMemberService(
    private val clubMemberRepository: ClubMemberRepository,
    private val clubRepository: ClubRepository,
) {
    fun createClubMembers(members: List<ClubMemberEntity>) {
        clubMemberRepository.saveAll(members)
    }

    fun createAndValidateClubMembers(members: List<ClubMemberEntity>) {
        members.forEach { member ->
            validateByClubAndStudent(member.club, member.studentId)
        }
        clubMemberRepository.saveAll(members)
    }

    fun setClubMemberStatus(clubMemberId: Long, studentId: Long, clubStatus: ClubStatus) {
        val clubMember = clubMemberRepository.findByIdAndStudentId(clubMemberId, studentId)
            ?: throw ClubMemberNotFoundException()

        if (clubStatus == ClubStatus.REJECTED && clubMember.club.type == ClubType.CREATIVE_ACTIVITY_CLUB) {
            rejectOtherCreativeClubs(clubMember.studentId)
        }

        clubMember.modifyStatus(clubStatus)
        clubMemberRepository.save(clubMember)
    }

    fun setStatusStudentClub(studentId: Long, clubId: Long, status: ClubStatus) {
        val club = clubRepository.findById(clubId)
            .orElseThrow { ClubNotFoundException() }
        val clubMember = clubMemberRepository.findByClubAndStudentIdAndClubStatusNot(club, studentId, ClubStatus.DELETED)
            ?: throw ClubMemberNotFoundException()
        clubMember.modifyStatus(status)
        clubMemberRepository.save(clubMember)
    }

    fun getAllLeaders(): List<ClubMemberEntity> =
        clubMemberRepository.findAllByPermission(ClubPermission.CLUB_LEADER)

    fun findClubIfNotClubMember(clubId: Long, state: ClubStatus, studentId: Long, excludeStatus: ClubStatus): ClubEntity {
        val club = clubRepository.findByIdAndStateNot(clubId, ClubStatus.DELETED)
            ?: throw ClubNotFoundException()
        val existingMember = clubMemberRepository.findByClubAndStudentIdAndClubStatusNot(club, studentId, excludeStatus)
        if (existingMember != null) {
            throw ClubJoinedException()
        }
        return club
    }

    fun findAllCreativeClubByStudent(studentId: Long): List<ClubMemberEntity> =
        clubMemberRepository.findByStudentIdAndClubStatusInAndClub_TypeAndClub_State(
            studentId,
            listOf(ClubStatus.PENDING, ClubStatus.ALLOWED),
            ClubType.CREATIVE_ACTIVITY_CLUB,
            ClubStatus.ALLOWED,
        )

    fun getPendingAndAllowedMembersByClubs(clubs: List<ClubEntity>): List<ClubMemberEntity> =
        clubMemberRepository.findByClubInAndClubStatusNotIn(
            clubs,
            ClubStatus.getNotAllowedStatuses(),
        )

    fun updateStatus(members: List<ClubMemberEntity>, status: ClubStatus) {
        members.forEach { member ->
            member.modifyStatus(status)

            if (status == ClubStatus.ALLOWED && member.club.type == ClubType.CREATIVE_ACTIVITY_CLUB) {
                rejectOtherCreativeClubs(member.studentId)
            }
        }
        clubMemberRepository.saveAll(members)
    }

    fun getStudentClubStatus(studentId: Long): List<ClubEntity> {
        val leaderMemberships = clubMemberRepository.findByStudentIdAndPermissionAndClub_StateNot(
            studentId,
            ClubPermission.CLUB_LEADER,
            ClubStatus.DELETED,
        )
        return leaderMemberships.map { it.club }
    }

    fun getJoinRequests(studentId: Long, status: ClubStatus): List<ClubMemberEntity> =
        clubMemberRepository.findByStudentIdAndClubStatus(studentId, status)

    fun getClubLeader(clubId: Long): ClubMemberEntity {
        val club = clubRepository.findById(clubId)
            .orElseThrow { ClubNotFoundException() }
        return clubMemberRepository.findByClubAndPermissionAndClubStatus(
            club,
            ClubPermission.CLUB_LEADER,
            ClubStatus.ALLOWED,
        ) ?: throw ClubMemberNotFoundException()
    }

    fun validateActiveClubMemberSize(club: ClubEntity, leaderStudentId: Long) {
        val leader = clubMemberRepository.findByClubAndStudentIdAndPermission(
            club,
            leaderStudentId,
            ClubPermission.CLUB_LEADER,
        ) ?: throw ClubPermissionDeniedException()

        val activeMembers = clubMemberRepository.findAllByClubAndClubStatus(club, ClubStatus.ALLOWED)
        if (activeMembers.size < 5) {
            throw InsufficientClubMembersException()
        }
    }

    fun getStatusClubMembers(clubId: Long, status: ClubStatus): List<ClubMemberEntity> {
        val club = clubRepository.findById(clubId)
            .orElseThrow { ClubNotFoundException() }
        return clubMemberRepository.findByClubAndClubStatus(club, status)
    }

    fun getAllClubMembers(clubId: Long): List<ClubMemberEntity> {
        val club = clubRepository.findById(clubId)
            .orElseThrow { ClubNotFoundException() }
        return clubMemberRepository.findByClub(club)
    }

    fun setDeleteAllClubMembers(club: ClubEntity) {
        val members = clubMemberRepository.findByClub(club)
        members.forEach { it.modifyStatus(ClubStatus.DELETED) }
        clubMemberRepository.saveAll(members)
    }

    fun setDeleteClubMembers(club: ClubEntity) {
        val members = clubMemberRepository.findByClubAndClubStatusNot(club, ClubStatus.ALLOWED)
        members.forEach { it.modifyStatus(ClubStatus.DELETED) }
        clubMemberRepository.saveAll(members)
    }

    fun validateAndRejectLeader(club: ClubEntity, leaderStudentId: Long, studentIds: List<Long>) {
        if (club.type == ClubType.CREATIVE_ACTIVITY_CLUB) {
            studentIds.forEach { studentId ->
                val isJoined = isCreativeClubJoined(studentId)
                if (isJoined) {
                    throw InvalidClubMemberInviteException()
                }
            }
        }

        if (studentIds.contains(leaderStudentId)) {
            throw InvalidClubMemberInviteException()
        }

        if (studentIds.size != studentIds.distinct().size) {
            throw InvalidClubMemberInviteException()
        }
    }

    fun validateByClubLeader(club: ClubEntity, studentId: Long) {
        val isLeader = clubMemberRepository.existsByClubAndStudentIdAndPermission(
            club,
            studentId,
            ClubPermission.CLUB_LEADER,
        )
        if (!isLeader) {
            throw ClubPermissionDeniedException()
        }
    }

    fun validateByClubAndStudent(club: ClubEntity, studentId: Long) {
        val existingMember = clubMemberRepository.findByClubAndStudentIdAndClubStatusNot(
            club,
            studentId,
            ClubStatus.DELETED,
        )
        if (existingMember != null) {
            throw ClubJoinedException()
        }
    }

    fun isCreativeClubJoined(studentId: Long): Boolean {
        val memberships = clubMemberRepository.findByStudentIdAndClubStatusInAndClub_Type(
            studentId,
            listOf(ClubStatus.PENDING, ClubStatus.ALLOWED),
            ClubType.CREATIVE_ACTIVITY_CLUB,
        )
        return memberships.isNotEmpty()
    }

    fun isClubLeader(clubId: Long, studentId: Long): Boolean {
        val club = clubRepository.findById(clubId).orElse(null) ?: return false
        return clubMemberRepository.existsByClubAndStudentIdAndPermission(
            club,
            studentId,
            ClubPermission.CLUB_LEADER,
        )
    }

    fun shuffleClubMemberMap(
        maxCount: Int,
        clubMemberMap: Map<ClubEntity, List<ClubMemberEntity>>,
        priority: ClubPriority,
    ): List<ClubMemberEntity> {
        val selectedMembers = mutableListOf<ClubMemberEntity>()

        clubMemberMap.forEach { (_, members) ->
            val priorityMembers = members.filter { it.priority == priority }
            val shuffled = priorityMembers.shuffled()
            val selected = shuffled.take(maxCount)
            selectedMembers.addAll(selected)
        }

        return selectedMembers
    }

    private fun rejectOtherCreativeClubs(studentId: Long) {
        val otherMemberships = clubMemberRepository.findByStudentIdAndClubStatusAndClub_State(
            studentId,
            ClubStatus.PENDING,
            ClubStatus.ALLOWED,
        ).filter { it.club.type == ClubType.CREATIVE_ACTIVITY_CLUB }

        otherMemberships.forEach { it.modifyStatus(ClubStatus.REJECTED) }
        if (otherMemberships.isNotEmpty()) {
            clubMemberRepository.saveAll(otherMemberships)
        }
    }
}
