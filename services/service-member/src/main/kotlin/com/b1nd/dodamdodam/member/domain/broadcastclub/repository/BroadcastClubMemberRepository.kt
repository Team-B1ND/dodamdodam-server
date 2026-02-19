package com.b1nd.dodamdodam.member.domain.broadcastclub.repository

import com.b1nd.dodamdodam.member.domain.broadcastclub.entity.BroadcastClubMemberEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BroadcastClubMemberRepository : JpaRepository<BroadcastClubMemberEntity, Long> {
    fun findByMember(member: MemberEntity): BroadcastClubMemberEntity?
    fun existsByMember(member: MemberEntity): Boolean
}
