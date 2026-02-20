package com.b1nd.dodamdodam.member.domain.teacher.service

import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.exception.TeacherNotFoundException
import com.b1nd.dodamdodam.member.domain.teacher.entity.TeacherEntity
import com.b1nd.dodamdodam.member.domain.teacher.repository.TeacherRepository
import org.springframework.stereotype.Service

@Service
class TeacherService(
    private val teacherRepository: TeacherRepository,
) {
    fun create(teacher: TeacherEntity): TeacherEntity = teacherRepository.save(teacher)

    fun getByMember(member: MemberEntity): TeacherEntity =
        teacherRepository.findByMember(member) ?: throw TeacherNotFoundException()

    fun findByMember(member: MemberEntity): TeacherEntity? = teacherRepository.findByMember(member)

    fun delete(teacher: TeacherEntity) = teacherRepository.delete(teacher)
}
