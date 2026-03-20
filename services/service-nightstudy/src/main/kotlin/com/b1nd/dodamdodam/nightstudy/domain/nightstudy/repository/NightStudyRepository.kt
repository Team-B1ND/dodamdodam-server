package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatusType
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NightStudyRepository: JpaRepository<NightStudyEntity, Long> {
    fun findAllByLeaderIdAndStatusAndType(leaderId: UUID, status: NightStudyStatusType, type: NightStudyType): List<NightStudyEntity>
    fun findAllByIdsAndStatusAndType(ids: List<Long>, status: NightStudyStatusType, type: NightStudyType): List<NightStudyEntity>
    fun findAllByType(type: NightStudyType): List<NightStudyEntity>
    fun existsByIdAndLeaderId(id: Long, leaderId: UUID): Boolean
}