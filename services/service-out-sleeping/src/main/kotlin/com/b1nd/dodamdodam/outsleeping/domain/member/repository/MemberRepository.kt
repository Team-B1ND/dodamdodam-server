package com.b1nd.dodamdodam.outsleeping.domain.member.repository

import com.b1nd.dodamdodam.outsleeping.domain.member.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberRepository : JpaRepository<MemberEntity, Long> {

    fun findByUserId(userId: UUID): MemberEntity?

    fun findAllByUserIdIn(userIds: Collection<UUID>): List<MemberEntity>

    fun findAllByRole(role: String): List<MemberEntity>
}
