package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.service

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyMemberEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyNotFoundException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository.NightStudyMemberRepository
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository.NightStudyRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class NightStudyService(
    private val nightStudyRepository: NightStudyRepository,
    private val nightStudyMemberRepository: NightStudyMemberRepository
) {

    companion object {
        val INDIVIDUAL_TYPES = listOf(NightStudyType.NIGHT_STUDY_1, NightStudyType.NIGHT_STUDY_2, NightStudyType.NIGHT_STUDY_3)
        val PROJECT_TYPES = listOf(NightStudyType.PROJECT_1, NightStudyType.PROJECT_2)
    }

    fun save(nightStudy: NightStudyEntity): NightStudyEntity =
        nightStudyRepository.save(nightStudy)

    fun getById(id: Long): NightStudyEntity =
        nightStudyRepository.findById(id).orElseThrow { NightStudyNotFoundException() }

    fun softDelete(nightStudy: NightStudyEntity) {
        nightStudy.softDelete()
        nightStudyRepository.save(nightStudy)
    }

    // Individual night study queries
    fun getByUserId(userId: UUID): List<NightStudyEntity> =
        nightStudyRepository.findAllByUserIdAndTypeIn(userId, INDIVIDUAL_TYPES)

    fun getAllowed(date: LocalDate): List<NightStudyEntity> =
        nightStudyRepository.findAllByStatusAndTypeInAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            NightStudyStatus.ALLOWED, INDIVIDUAL_TYPES, date, date
        )

    fun getPending(): List<NightStudyEntity> =
        nightStudyRepository.findAllByStatusAndTypeIn(NightStudyStatus.PENDING, INDIVIDUAL_TYPES)

    fun getAll(): List<NightStudyEntity> =
        nightStudyRepository.findAllByTypeIn(INDIVIDUAL_TYPES)

    fun getProjectsByUserId(userId: UUID): List<NightStudyEntity> =
        nightStudyRepository.findAllByUserIdAndTypeIn(userId, PROJECT_TYPES)

    fun getAllowedProjects(date: LocalDate): List<NightStudyEntity> =
        nightStudyRepository.findAllByStatusAndTypeInAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            NightStudyStatus.ALLOWED, PROJECT_TYPES, date, date
        )

    fun getPendingProjects(): List<NightStudyEntity> =
        nightStudyRepository.findAllByStatusAndTypeIn(NightStudyStatus.PENDING, PROJECT_TYPES)

    // Member management
    fun saveMembers(members: List<NightStudyMemberEntity>): List<NightStudyMemberEntity> =
        nightStudyMemberRepository.saveAll(members)

    fun getMembersByNightStudyId(nightStudyId: Long): List<NightStudyMemberEntity> =
        nightStudyMemberRepository.findAllByNightStudyId(nightStudyId)

    fun getMembersByNightStudyIds(nightStudyIds: List<Long>): List<NightStudyMemberEntity> =
        nightStudyMemberRepository.findAllByNightStudyIdIn(nightStudyIds)
}
