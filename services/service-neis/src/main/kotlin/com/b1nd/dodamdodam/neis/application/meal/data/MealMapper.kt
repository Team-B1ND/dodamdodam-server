package com.b1nd.dodamdodam.neis.application.meal.data

import com.b1nd.dodamdodam.neis.application.meal.data.response.MealResponse
import com.b1nd.dodamdodam.neis.domain.meal.entity.MealEntity

fun MealEntity.toResponse() = MealResponse(
    date = date,
    mealType = mealType,
    calorie = calorie,
    menus = getMenuList(),
)