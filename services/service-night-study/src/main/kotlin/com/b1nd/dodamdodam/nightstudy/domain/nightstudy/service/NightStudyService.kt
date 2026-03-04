package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.service

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyNotFoundException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository.NightStudyRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class NightStudyService(
    private val nightStudyRepository: NightStudyRepository
) {

    fun save(nightStudy: NightStudyEntity): NightStudyEntity =
        nightStudyRepository.save(nightStudy)

    fun getById(id: Long): NightStudyEntity =
        nightStudyRepository.findById(id).orElseThrow { NightStudyNotFoundException() }

    fun getByUserId(userId: UUID): List<NightStudyEntity> =
        nightStudyRepository.findAllByUserId(userId)

    fun getAllowed(date: LocalDate): List<NightStudyEntity> =
        nightStudyRepository.findAllByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            NightStudyStatus.ALLOWED, date, date
        )

    fun getPending(): List<NightStudyEntity> =
        nightStudyRepository.findAllByStatus(NightStudyStatus.PENDING)

    fun getAll(): List<NightStudyEntity> =
        nightStudyRepository.findAll()

    fun delete(nightStudy: NightStudyEntity) =
        nightStudyRepository.delete(nightStudy)
}
