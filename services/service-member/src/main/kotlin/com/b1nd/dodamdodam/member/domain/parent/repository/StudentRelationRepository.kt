package com.b1nd.dodamdodam.member.domain.parent.repository

import com.b1nd.dodamdodam.member.domain.parent.entity.ParentEntity
import com.b1nd.dodamdodam.member.domain.parent.entity.StudentRelationEntity
import com.b1nd.dodamdodam.member.domain.student.entity.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRelationRepository : JpaRepository<StudentRelationEntity, Long> {
    fun findByStudent(student: StudentEntity): List<StudentRelationEntity>
    fun findByParent(parent: ParentEntity): List<StudentRelationEntity>
}
