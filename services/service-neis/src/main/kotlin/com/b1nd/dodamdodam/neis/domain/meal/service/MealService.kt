package com.b1nd.dodamdodam.neis.domain.meal.service

import com.b1nd.dodamdodam.neis.domain.meal.entity.MealEntity
import com.b1nd.dodamdodam.neis.domain.meal.enumeration.MealType
import com.b1nd.dodamdodam.neis.domain.meal.repository.MealRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MealService(
    private val mealRepository: MealRepository,
) {
    fun getMealsByDate(date: LocalDate): List<MealEntity> =
        mealRepository.findAllByDateOrderByMealTypeAsc(date)

    fun saveOrUpdate(date: LocalDate, mealType: MealType, calorie: Double, menus: String) {
        val mealEntity = mealRepository.findByDateAndMealType(date, mealType)
            ?.apply { updateMenu(calorie, menus) }
            ?: MealEntity(date, mealType, calorie, menus)

        mealRepository.save(mealEntity)
    }
}