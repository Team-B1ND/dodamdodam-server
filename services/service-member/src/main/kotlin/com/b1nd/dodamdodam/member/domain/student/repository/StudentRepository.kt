package com.b1nd.dodamdodam.member.domain.student.repository

import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.student.entity.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<StudentEntity, Long> {
    fun findByMember(member: MemberEntity): StudentEntity?
    fun findByGrade(grade: Int): List<StudentEntity>
}
