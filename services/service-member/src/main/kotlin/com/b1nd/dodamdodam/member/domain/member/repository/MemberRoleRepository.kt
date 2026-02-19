package com.b1nd.dodamdodam.member.domain.member.repository

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberRoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRoleRepository : JpaRepository<MemberRoleEntity, Long> {
    fun findByMember(member: MemberEntity): List<MemberRoleEntity>
    fun findByMemberAndRole(member: MemberEntity, role: RoleType): MemberRoleEntity?
    fun deleteByMember(member: MemberEntity)
}
