package com.b1nd.dodamdodam.user.domain.student.repository

import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository: JpaRepository<StudentEntity, Long> {
}