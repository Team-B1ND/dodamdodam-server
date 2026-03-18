package com.b1nd.dodamdodam.meal.application.meal.data.response

import com.b1nd.dodamdodam.meal.domain.meal.enumeration.MealType
import java.time.LocalDate

data class MealResponse(
    val date: LocalDate,
    val mealType: MealType,
    val calorie: Double,
    val menus: List<String>,
)