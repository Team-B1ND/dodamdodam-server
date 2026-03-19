package com.b1nd.dodamdodam.neis.infrastructure.scheduler

import com.b1nd.dodamdodam.neis.domain.meal.service.MealService
import com.b1nd.dodamdodam.neis.infrastructure.neis.NeisClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.YearMonth

@Component
class MealScheduler(
    private val neisClient: NeisClient,
    private val mealService: MealService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 3 L-2 * *")
    @Transactional
    fun fetchNextMonthMeals() {
        val nextMonth = YearMonth.now().plusMonths(1)
        syncMeals(nextMonth)
    }

    @Scheduled(cron = "0 0 4 1 * *")
    @Transactional
    fun fetchCurrentMonthMeals() {
        syncMeals(YearMonth.now())
    }

    private fun syncMeals(yearMonth: YearMonth) {
        try {
            val meals = neisClient.fetchMonthlyMeals(yearMonth)
            meals.forEach { meal ->
                mealService.saveOrUpdate(meal.date, meal.mealType, meal.calorie, meal.menus)
            }
        } catch (e: Exception) {
            log.error("급식 데이터 동기화 실패: {}", yearMonth, e)
        }
    }
}