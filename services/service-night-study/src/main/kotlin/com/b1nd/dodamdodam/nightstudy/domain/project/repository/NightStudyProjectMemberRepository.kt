package com.b1nd.dodamdodam.nightstudy.domain.project.repository

import com.b1nd.dodamdodam.nightstudy.domain.project.entity.NightStudyProjectMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NightStudyProjectMemberRepository : JpaRepository<NightStudyProjectMemberEntity, Long> {

    fun findAllByUserId(userId: UUID): List<NightStudyProjectMemberEntity>
}
