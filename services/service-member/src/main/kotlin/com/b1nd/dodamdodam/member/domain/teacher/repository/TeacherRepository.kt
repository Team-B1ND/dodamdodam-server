package com.b1nd.dodamdodam.member.domain.teacher.repository

import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.teacher.entity.TeacherEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository : JpaRepository<TeacherEntity, Long> {
    fun findByMember(member: MemberEntity): TeacherEntity?
}
