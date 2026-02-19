package com.b1nd.dodamdodam.member.domain.student.service

import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.member.exception.StudentNotFoundException
import com.b1nd.dodamdodam.member.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.member.domain.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {
    fun save(student: StudentEntity): StudentEntity = studentRepository.save(student)

    fun getByMember(member: MemberEntity): StudentEntity =
        studentRepository.findByMember(member) ?: throw StudentNotFoundException()

    fun findByMember(member: MemberEntity): StudentEntity? = studentRepository.findByMember(member)

    fun findByGrade(grade: Int): List<StudentEntity> = studentRepository.findByGrade(grade)

    fun delete(student: StudentEntity) = studentRepository.delete(student)
}
