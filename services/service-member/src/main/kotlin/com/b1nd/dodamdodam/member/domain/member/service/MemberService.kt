package com.b1nd.dodamdodam.member.domain.member.service

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberRoleEntity
import com.b1nd.dodamdodam.member.domain.member.enumeration.ActiveStatus
import com.b1nd.dodamdodam.member.domain.member.exception.MemberNotFoundException
import com.b1nd.dodamdodam.member.domain.member.repository.MemberRepository
import com.b1nd.dodamdodam.member.domain.member.repository.MemberRoleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
) {
    fun create(member: MemberEntity): MemberEntity = memberRepository.save(member)

    fun getById(id: Long): MemberEntity =
        memberRepository.findById(id).orElseThrow { MemberNotFoundException() }

    fun getByUsername(username: String): MemberEntity =
        memberRepository.findByUsername(username) ?: throw MemberNotFoundException()

    fun existsByUsername(username: String): Boolean = memberRepository.existsByUsername(username)

    fun existsByEmail(email: String): Boolean = memberRepository.existsByEmail(email)

    fun findByStatus(status: ActiveStatus): List<MemberEntity> = memberRepository.findByStatus(status)

    fun findAll(): List<MemberEntity> = memberRepository.findAll()

    fun searchMembers(name: String?, role: RoleType?, status: ActiveStatus?, pageable: Pageable): Page<MemberEntity> =
        memberRepository.searchMembers(name, role, status, pageable)

    fun searchByNameAndRoleAndStatus(name: String, role: RoleType, status: ActiveStatus, pageable: Pageable): Page<MemberEntity> =
        memberRepository.searchByNameAndRoleAndStatus(name, role, status, pageable)

    fun delete(member: MemberEntity) {
        memberRoleRepository.deleteByMember(member)
        memberRepository.delete(member)
    }

    fun createRole(memberRole: MemberRoleEntity): MemberRoleEntity = memberRoleRepository.save(memberRole)

    fun getRoles(member: MemberEntity): List<MemberRoleEntity> = memberRoleRepository.findByMember(member)

    fun findRole(member: MemberEntity, role: RoleType): MemberRoleEntity? =
        memberRoleRepository.findByMemberAndRole(member, role)

    fun deleteRoles(member: MemberEntity) = memberRoleRepository.deleteByMember(member)
}
