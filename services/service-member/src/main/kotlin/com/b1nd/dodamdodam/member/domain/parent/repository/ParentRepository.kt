package com.b1nd.dodamdodam.member.domain.parent.repository

import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.parent.entity.ParentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParentRepository : JpaRepository<ParentEntity, Long> {
    fun findByMember(member: MemberEntity): ParentEntity?
}
