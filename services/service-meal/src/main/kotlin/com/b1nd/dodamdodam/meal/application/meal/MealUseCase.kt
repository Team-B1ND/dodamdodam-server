package com.b1nd.dodamdodam.meal.application.meal

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.meal.application.meal.data.response.MealResponse
import com.b1nd.dodamdodam.meal.application.meal.data.toResponse
import com.b1nd.dodamdodam.meal.domain.meal.service.MealService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(readOnly = true)
class MealUseCase(
    private val mealService: MealService,
) {
    fun getMealsByDate(date: LocalDate): Response<List<MealResponse>> {
        val meals = mealService.getMealsByDate(date)
        return Response.ok("급식을 조회했어요.", meals.map { it.toResponse() })
    }
}