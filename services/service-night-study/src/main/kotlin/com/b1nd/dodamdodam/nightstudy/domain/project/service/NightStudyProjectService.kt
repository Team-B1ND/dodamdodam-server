package com.b1nd.dodamdodam.nightstudy.domain.project.service

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.project.entity.NightStudyProjectEntity
import com.b1nd.dodamdodam.nightstudy.domain.project.exception.NightStudyProjectNotFoundException
import com.b1nd.dodamdodam.nightstudy.domain.project.repository.NightStudyProjectRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class NightStudyProjectService(
    private val nightStudyProjectRepository: NightStudyProjectRepository
) {

    fun save(project: NightStudyProjectEntity): NightStudyProjectEntity =
        nightStudyProjectRepository.save(project)

    fun getById(id: Long): NightStudyProjectEntity =
        nightStudyProjectRepository.findById(id).orElseThrow { NightStudyProjectNotFoundException() }

    fun getByUserId(userId: UUID): List<NightStudyProjectEntity> =
        nightStudyProjectRepository.findAllByUserId(userId)

    fun getAllowed(date: LocalDate): List<NightStudyProjectEntity> =
        nightStudyProjectRepository.findAllByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            NightStudyStatus.ALLOWED, date, date
        )

    fun getPending(): List<NightStudyProjectEntity> =
        nightStudyProjectRepository.findAllByStatus(NightStudyStatus.PENDING)

    fun delete(project: NightStudyProjectEntity) =
        nightStudyProjectRepository.delete(project)
}
