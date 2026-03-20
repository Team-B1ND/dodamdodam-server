package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NightStudyMemberRepository: JpaRepository<NightStudyMemberEntity, Long> {
    fun findAllByUserId(userId: UUID): List<NightStudyMemberEntity>
    fun deleteAllByNightStudyId(id: Long)
    fun findAllByNightStudyId(id: Long): List<UUID>
}