package com.b1nd.dodamdodam.user.domain.teacher.repository

import com.b1nd.dodamdodam.user.domain.teacher.entity.TeacherEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository: JpaRepository<TeacherEntity, Long> {
}