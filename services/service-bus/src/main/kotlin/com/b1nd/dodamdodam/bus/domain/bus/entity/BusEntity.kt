package com.b1nd.dodamdodam.bus.domain.bus.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "buses")
class BusEntity(
    @Column(nullable = false)
    var name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun modifyName(name: String) {
        this.name = name
    }
}
