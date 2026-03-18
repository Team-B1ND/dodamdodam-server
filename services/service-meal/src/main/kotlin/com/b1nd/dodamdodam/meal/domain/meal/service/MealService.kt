package com.b1nd.dodamdodam.meal.domain.meal.service

import com.b1nd.dodamdodam.meal.domain.meal.entity.MealEntity
import com.b1nd.dodamdodam.meal.domain.meal.enumeration.MealType
import com.b1nd.dodamdodam.meal.domain.meal.exception.MealNotFoundException
import com.b1nd.dodamdodam.meal.domain.meal.repository.MealRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MealService(
    private val mealRepository: MealRepository,
) {
    fun getMealsByDate(date: LocalDate): List<MealEntity> =
        mealRepository.findAllByDateOrderByMealTypeAsc(date)

    fun saveOrUpdate(date: LocalDate, mealType: MealType, calorie: Double, menus: String) {
        val existing = mealRepository.findByDateAndMealType(date, mealType)
        if (existing != null) {
            existing.updateMenu(calorie, menus)
        } else {
            mealRepository.save(
                MealEntity(
                    date = date,
                    mealType = mealType,
                    calorie = calorie,
                    menus = menus,
                )
            )
        }
    }
}