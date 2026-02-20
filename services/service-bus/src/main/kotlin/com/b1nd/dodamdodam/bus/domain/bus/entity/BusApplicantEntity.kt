package com.b1nd.dodamdodam.bus.domain.bus.entity

import com.b1nd.dodamdodam.bus.domain.bus.enumeration.BoardingType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "bus_applicants")
class BusApplicantEntity(
    @Column(nullable = false)
    val userId: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_bus_id", nullable = false)
    val bus: BusEntity,

    var seat: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var boardingType: BoardingType = BoardingType.BEFORE_BOARDING
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateBoardingType(boardingType: BoardingType) {
        if (boardingType == BoardingType.UNBOARDED) {
            this.seat = null
        }
        this.boardingType = boardingType
    }

    fun updateSeat(seat: Int) {
        this.seat = seat
    }
}
