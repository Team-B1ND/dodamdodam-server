package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.service

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyMemberEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatusType
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyBannedException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception.NightStudyNotFoundException
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository.NightStudyBannedRepository
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository.NightStudyMemberRepository
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository.NightStudyRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NightStudyService(
    private val nightStudyRepository: NightStudyRepository,
    private val nightStudyMemberRepository: NightStudyMemberRepository,
    private val bannedRepository: NightStudyBannedRepository
) {
    @Transactional
    fun save(nightStudy: NightStudyEntity, members: List<UUID>?) {
        if(isBanned(nightStudy.leaderId)) throw NightStudyBannedException()
        members?.forEach { member ->
            if (isBanned(member)) throw NightStudyBannedException()
        }

        val saved = nightStudyRepository.save(nightStudy)
        members?.forEach { member ->
            nightStudyMemberRepository.save(NightStudyMemberEntity(saved.id!!, member))
        }
    }

    fun findAllByUserIdAndStatusAndType(userId: UUID, status: NightStudyStatusType, type: NightStudyType): List<NightStudyEntity> {
        val asLeader = findAllByLeaderId(userId, status, type)

        val asMember = findAllByMemberId(userId, status, type)

        return (asLeader + asMember).sortedBy { it.id }
    }

    fun findAllByType(type: NightStudyType): List<NightStudyEntity> {
        return nightStudyRepository.findAllByType(type)
    }

    fun findById(id: Long): NightStudyEntity? {
        return nightStudyRepository.findById(id).orElseThrow { NightStudyNotFoundException() }
    }

    fun findMembersByNightStudyId(id: Long): List<UUID> {
        return nightStudyMemberRepository.findAllByNightStudyId(id)
    }

    @Transactional
    fun delete(userId: UUID, id: Long) {
        if()
        nightStudyRepository.deleteById(id)
        nightStudyMemberRepository.deleteAllByNightStudyId(id)
    }

    fun allow(id: Long) {
        nightStudyRepository.findById(id).ifPresent { it.allow() }
    }

    fun reject(id: Long, rejectionReason: String) {
        nightStudyRepository.findById(id).ifPresent { it.reject(rejectionReason) }
    }

    fun pending(id: Long) {
        nightStudyRepository.findById(id).ifPresent { it.pending() }
    }

    private fun findAllByLeaderId(leaderId: UUID, status: NightStudyStatusType, type: NightStudyType): List<NightStudyEntity> {
        return nightStudyRepository.findAllByLeaderIdAndStatusAndType(leaderId, status, type)
    }

    private fun findAllByMemberId(memberId: UUID, status: NightStudyStatusType, type: NightStudyType): List<NightStudyEntity> {
        val ids = nightStudyMemberRepository.findAllByUserId(memberId).map { it.nightStudyId }
        return nightStudyRepository.findAllByIdsAndStatusAndType(ids, status, type)
    }

    private fun isBanned(userId: UUID): Boolean {
        return bannedRepository.existsByUserId(userId)
    }
}