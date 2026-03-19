package com.b1nd.dodamdodam.neis.domain.meal.entity

import com.b1nd.dodamdodam.core.jpa.entity.BaseTimeEntity
import com.b1nd.dodamdodam.neis.domain.meal.enumeration.MealType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

@Entity
@Table(
    name = "meals",
    uniqueConstraints = [UniqueConstraint(columnNames = ["meal_date", "meal_type"])]
)
class MealEntity(
    @Column(name = "meal_date", nullable = false)
    val date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 16)
    val mealType: MealType,

    @Column(nullable = false)
    var calorie: Double,

    @Column(nullable = false, columnDefinition = "TEXT")
    var menus: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun getMenuList(): List<String> =
        menus.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    fun updateMenu(calorie: Double, menus: String) {
        this.calorie = calorie
        this.menus = menus
    }
}