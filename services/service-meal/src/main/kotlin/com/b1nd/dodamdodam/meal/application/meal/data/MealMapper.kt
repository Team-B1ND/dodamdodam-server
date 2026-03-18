package com.b1nd.dodamdodam.meal.application.meal.data

import com.b1nd.dodamdodam.meal.application.meal.data.response.MealResponse
import com.b1nd.dodamdodam.meal.domain.meal.entity.MealEntity

fun MealEntity.toResponse() = MealResponse(
    date = date,
    mealType = mealType,
    calorie = calorie,
    menus = getMenuList(),
)