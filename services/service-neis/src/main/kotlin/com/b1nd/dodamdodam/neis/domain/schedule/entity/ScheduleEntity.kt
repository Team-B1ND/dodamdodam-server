package com.b1nd.dodamdodam.neis.domain.schedule.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

@Entity
@Table(
    name = "schedules",
    uniqueConstraints = [UniqueConstraint(columnNames = ["schedule_date", "grade", "room", "period"])]
)
class ScheduleEntity(
    @Column(name = "schedule_date", nullable = false)
    val date: LocalDate,

    @Column(nullable = false)
    val grade: Int,

    @Column(name = "room", nullable = false)
    val room: Int,

    @Column(nullable = false)
    val period: Int,

    @Column(nullable = false, length = 100)
    var subject: String,

    @Column(nullable = false, length = 50)
    var teacher: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateSchedule(subject: String, teacher: String) {
        this.subject = subject
        this.teacher = teacher
    }
}