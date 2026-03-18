package com.b1nd.dodamdodam.meal.domain.meal.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class MealNotFoundException : BasicException(MealExceptionCode.MEAL_NOT_FOUND)
class NeisApiFetchFailedException : BasicException(MealExceptionCode.NEIS_API_FAILED)