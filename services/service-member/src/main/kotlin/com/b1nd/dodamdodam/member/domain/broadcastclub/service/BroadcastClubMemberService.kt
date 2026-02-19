package com.b1nd.dodamdodam.member.domain.broadcastclub.service

import com.b1nd.dodamdodam.member.domain.broadcastclub.entity.BroadcastClubMemberEntity
import com.b1nd.dodamdodam.member.domain.broadcastclub.repository.BroadcastClubMemberRepository
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.exception.BroadcastClubMemberNotFoundException
import org.springframework.stereotype.Service

@Service
class BroadcastClubMemberService(
    private val broadcastClubMemberRepository: BroadcastClubMemberRepository,
) {
    fun save(broadcastClubMember: BroadcastClubMemberEntity): BroadcastClubMemberEntity =
        broadcastClubMemberRepository.save(broadcastClubMember)

    fun getByMember(member: MemberEntity): BroadcastClubMemberEntity =
        broadcastClubMemberRepository.findByMember(member) ?: throw BroadcastClubMemberNotFoundException()

    fun existsByMember(member: MemberEntity): Boolean = broadcastClubMemberRepository.existsByMember(member)

    fun delete(broadcastClubMember: BroadcastClubMemberEntity) =
        broadcastClubMemberRepository.delete(broadcastClubMember)
}
