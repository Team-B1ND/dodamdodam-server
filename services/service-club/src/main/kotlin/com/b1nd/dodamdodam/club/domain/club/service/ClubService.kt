package com.b1nd.dodamdodam.club.domain.club.service

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.entity.ClubTimeEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPermission
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubTimeType
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import com.b1nd.dodamdodam.club.domain.club.exception.ClubApplicationDurationPassedException
import com.b1nd.dodamdodam.club.domain.club.exception.ClubDuplicateException
import com.b1nd.dodamdodam.club.domain.club.exception.ClubNotFoundException
import com.b1nd.dodamdodam.club.domain.club.repository.ClubMemberRepository
import com.b1nd.dodamdodam.club.domain.club.repository.ClubRepository
import com.b1nd.dodamdodam.club.domain.club.repository.ClubTimeRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val clubTimeRepository: ClubTimeRepository,
    private val clubMemberRepository: ClubMemberRepository,
) {
    fun checkIsNameDuplicated(name: String) {
        if (clubRepository.existsByName(name)) {
            throw ClubDuplicateException()
        }
    }

    fun getCreativeActivityClubs(): List<ClubEntity> {
        val clubs = clubRepository.findByTypeAndState(ClubType.CREATIVE_ACTIVITY_CLUB, ClubStatus.ALLOWED)
        if (clubs.isEmpty()) {
            throw ClubNotFoundException()
        }
        return clubs
    }

    fun save(club: ClubEntity): ClubEntity = clubRepository.save(club)

    fun saveAll(clubs: List<ClubEntity>): List<ClubEntity> = clubRepository.saveAll(clubs)

    fun saveClubAndMembers(club: ClubEntity, leaderStudentId: Long, studentIds: List<Long>) {
        val savedClub = clubRepository.save(club)

        val leader = ClubMemberEntity(
            permission = ClubPermission.CLUB_LEADER,
            clubStatus = ClubStatus.ALLOWED,
            studentId = leaderStudentId,
            club = savedClub,
        )

        val members = studentIds.map { studentId ->
            ClubMemberEntity(
                permission = ClubPermission.CLUB_MEMBER,
                clubStatus = ClubStatus.WAITING,
                studentId = studentId,
                club = savedClub,
            )
        }

        clubMemberRepository.save(leader)
        clubMemberRepository.saveAll(members)
    }

    fun findAll(): List<ClubEntity> = clubRepository.findAllByStateNot(ClubStatus.DELETED)

    fun findById(id: Long): ClubEntity =
        clubRepository.findByIdAndStateNot(id, ClubStatus.DELETED)
            ?: throw ClubNotFoundException()

    fun findByIds(ids: List<Long>): List<ClubEntity> = clubRepository.findByIdIn(ids)

    fun deleteClub(club: ClubEntity) {
        club.updateStatus(club.name + "_deleted_" + System.currentTimeMillis(), ClubStatus.DELETED)
    }

    fun validateApplicationDuration(type: ClubTimeType) {
        val clubTime = clubTimeRepository.findById(type)
            .orElseThrow { ClubApplicationDurationPassedException() }
        val today = LocalDate.now()
        if (today.isBefore(clubTime.start) || today.isAfter(clubTime.end)) {
            throw ClubApplicationDurationPassedException()
        }
    }

    fun getClubTime(type: ClubTimeType): ClubTimeEntity =
        clubTimeRepository.findById(type)
            .orElseThrow { ClubApplicationDurationPassedException() }

    fun setClubTime(clubTime: ClubTimeEntity) {
        clubTimeRepository.save(clubTime)
    }
}
