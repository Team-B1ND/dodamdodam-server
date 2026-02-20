package com.b1nd.dodamdodam.member.domain.parent.service

import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.exception.MemberNotFoundException
import com.b1nd.dodamdodam.member.domain.parent.entity.ParentEntity
import com.b1nd.dodamdodam.member.domain.parent.entity.StudentRelationEntity
import com.b1nd.dodamdodam.member.domain.parent.repository.ParentRepository
import com.b1nd.dodamdodam.member.domain.parent.repository.StudentRelationRepository
import com.b1nd.dodamdodam.member.domain.student.entity.StudentEntity
import org.springframework.stereotype.Service

@Service
class ParentService(
    private val parentRepository: ParentRepository,
    private val studentRelationRepository: StudentRelationRepository,
) {
    fun create(parent: ParentEntity): ParentEntity = parentRepository.save(parent)

    fun getByMember(member: MemberEntity): ParentEntity =
        parentRepository.findByMember(member) ?: throw MemberNotFoundException()

    fun findByMember(member: MemberEntity): ParentEntity? = parentRepository.findByMember(member)

    fun saveRelation(relation: StudentRelationEntity): StudentRelationEntity =
        studentRelationRepository.save(relation)

    fun getRelationsByStudent(student: StudentEntity): List<StudentRelationEntity> =
        studentRelationRepository.findByStudent(student)

    fun getRelationsByParent(parent: ParentEntity): List<StudentRelationEntity> =
        studentRelationRepository.findByParent(parent)

    fun delete(parent: ParentEntity) = parentRepository.delete(parent)
}
