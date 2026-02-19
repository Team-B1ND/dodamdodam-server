package com.b1nd.dodamdodam.member.domain.dormitory.repository

import com.b1nd.dodamdodam.member.domain.dormitory.entity.DormitoryManageMemberEntity
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DormitoryManageMemberRepository : JpaRepository<DormitoryManageMemberEntity, Long> {
    fun findByMember(member: MemberEntity): DormitoryManageMemberEntity?
    fun existsByMember(member: MemberEntity): Boolean
}
