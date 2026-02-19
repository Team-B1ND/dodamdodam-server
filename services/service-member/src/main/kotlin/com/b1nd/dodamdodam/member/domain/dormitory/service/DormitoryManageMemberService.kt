package com.b1nd.dodamdodam.member.domain.dormitory.service

import com.b1nd.dodamdodam.member.domain.dormitory.entity.DormitoryManageMemberEntity
import com.b1nd.dodamdodam.member.domain.dormitory.repository.DormitoryManageMemberRepository
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.exception.DormitoryManageMemberNotFoundException
import org.springframework.stereotype.Service

@Service
class DormitoryManageMemberService(
    private val dormitoryManageMemberRepository: DormitoryManageMemberRepository,
) {
    fun save(dormitoryManageMember: DormitoryManageMemberEntity): DormitoryManageMemberEntity =
        dormitoryManageMemberRepository.save(dormitoryManageMember)

    fun getByMember(member: MemberEntity): DormitoryManageMemberEntity =
        dormitoryManageMemberRepository.findByMember(member) ?: throw DormitoryManageMemberNotFoundException()

    fun existsByMember(member: MemberEntity): Boolean = dormitoryManageMemberRepository.existsByMember(member)

    fun delete(dormitoryManageMember: DormitoryManageMemberEntity) =
        dormitoryManageMemberRepository.delete(dormitoryManageMember)
}
