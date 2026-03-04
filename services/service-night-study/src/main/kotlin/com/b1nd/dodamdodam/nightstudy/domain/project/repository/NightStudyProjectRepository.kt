package com.b1nd.dodamdodam.nightstudy.domain.project.repository

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.project.entity.NightStudyProjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface NightStudyProjectRepository : JpaRepository<NightStudyProjectEntity, Long> {

    fun findAllByUserId(userId: UUID): List<NightStudyProjectEntity>

    fun findAllByStatus(status: NightStudyStatus): List<NightStudyProjectEntity>

    fun findAllByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
        status: NightStudyStatus,
        startAt: LocalDate,
        endAt: LocalDate
    ): List<NightStudyProjectEntity>
}
