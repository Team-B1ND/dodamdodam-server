package com.b1nd.dodamdodam.neis.application.meal

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.neis.application.meal.data.response.MealResponse
import com.b1nd.dodamdodam.neis.application.meal.data.toResponse
import com.b1nd.dodamdodam.neis.domain.meal.service.MealService
import com.b1nd.dodamdodam.neis.infrastructure.neis.NeisClient
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.YearMonth

@Component
@Transactional(readOnly = true)
class MealUseCase(
    private val mealService: MealService,
    private val neisClient: NeisClient,
) {
    fun getMealsByDate(date: LocalDate): Response<List<MealResponse>> {
        val meals = mealService.getMealsByDate(date)
        return Response.ok("급식을 조회했어요.", meals.map { it.toResponse() })
    }

    @Transactional
    fun syncMeals(yearMonth: YearMonth): Response<Any> {
        val meals = neisClient.fetchMonthlyMeals(yearMonth)
        meals.forEach { meal ->
            mealService.saveOrUpdate(meal.date, meal.mealType, meal.calorie, meal.menus)
        }
        return Response.ok("${yearMonth} 급식 동기화가 완료되었어요.")
    }
}