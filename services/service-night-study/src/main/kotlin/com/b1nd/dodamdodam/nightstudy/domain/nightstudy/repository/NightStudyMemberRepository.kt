package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyMemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NightStudyMemberRepository : JpaRepository<NightStudyMemberEntity, Long> {

    fun findAllByNightStudyId(nightStudyId: Long): List<NightStudyMemberEntity>

    fun findAllByNightStudyIdIn(nightStudyIds: List<Long>): List<NightStudyMemberEntity>
}
