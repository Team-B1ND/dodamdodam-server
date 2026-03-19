package com.b1nd.dodamdodam.neis.domain.meal.repository

import com.b1nd.dodamdodam.neis.domain.meal.entity.MealEntity
import com.b1nd.dodamdodam.neis.domain.meal.enumeration.MealType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface MealRepository : JpaRepository<MealEntity, Long> {
    fun findAllByDateOrderByMealTypeAsc(date: LocalDate): List<MealEntity>
    fun findByDateAndMealType(date: LocalDate, mealType: MealType): MealEntity?
}