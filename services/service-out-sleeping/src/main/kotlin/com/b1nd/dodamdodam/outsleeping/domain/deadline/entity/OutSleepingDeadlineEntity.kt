package com.b1nd.dodamdodam.outsleeping.domain.deadline.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalTime

@Entity
@Table(name = "out_sleeping_deadlines")
class OutSleepingDeadlineEntity(

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, unique = true)
    var dayOfWeek: DayOfWeek,

    @Column(nullable = false)
    var time: LocalTime = LocalTime.of(17, 0),
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
