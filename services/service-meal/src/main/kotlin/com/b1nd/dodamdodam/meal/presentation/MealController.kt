package com.b1nd.dodamdodam.meal.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.meal.application.meal.MealUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/meal")
class MealController(
    private val mealUseCase: MealUseCase,
) {
    @UserAccess
    @GetMapping
    fun getMealsByDate(@RequestParam date: LocalDate) =
        mealUseCase.getMealsByDate(date)
}