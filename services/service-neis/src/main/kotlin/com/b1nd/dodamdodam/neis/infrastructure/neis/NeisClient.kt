package com.b1nd.dodamdodam.neis.infrastructure.neis

import com.b1nd.dodamdodam.neis.domain.meal.enumeration.MealType
import com.b1nd.dodamdodam.neis.infrastructure.config.MealProperties
import com.b1nd.dodamdodam.neis.infrastructure.neis.data.NeisMealApiResponse
import com.b1nd.dodamdodam.neis.infrastructure.neis.data.NeisMealRow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


data class ParsedMeal(
    val date: LocalDate,
    val mealType: MealType,
    val calorie: Double,
    val menus: String,
)

@Component
class NeisClient(
    private val restTemplate: RestTemplate,
    private val mealProperties: MealProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    fun fetchMonthlyMeals(yearMonth: YearMonth): List<ParsedMeal> {
        val startDate = yearMonth.atDay(1).format(dateFormatter)
        val endDate = yearMonth.atEndOfMonth().format(dateFormatter)

        val uri = UriComponentsBuilder
            .fromHttpUrl("https://open.neis.go.kr/hub/mealServiceDietInfo")
            .queryParam("KEY", mealProperties.neisApiKey)
            .queryParam("Type", "json")
            .queryParam("pIndex", 1)
            .queryParam("pSize", 100)
            .queryParam("ATPT_OFCDC_SC_CODE", mealProperties.eduOfficeCode)
            .queryParam("SD_SCHUL_CODE", mealProperties.schoolCode)
            .queryParam("MLSV_FROM_YMD", startDate)
            .queryParam("MLSV_TO_YMD", endDate)
            .build()
            .toUri()

        log.info("NEIS 요청 URL: {}", uri)
        val response = restTemplate.getForObject(uri, NeisMealApiResponse::class.java)
        val rows = response?.mealServiceDietInfo
            ?.flatMap { it.row ?: emptyList() }
            ?: emptyList()
        return rows.mapNotNull { parseRow(it) }
    }

    private fun parseRow(row: NeisMealRow): ParsedMeal? {
        val date = runCatching {
            LocalDate.parse(row.mealDate, dateFormatter)
        }.getOrElse {
            log.error("급식 날짜 파싱 실패: {}", row.mealDate, it)
            return null
        }

        val mealType = when (row.mealCode) {
            "1" -> MealType.BREAKFAST
            "2" -> MealType.LUNCH
            "3" -> MealType.DINNER
            else -> {
                log.error("알 수 없는 급식 코드: {}", row.mealCode)
                return null
            }
        }

        val menus = row.dishName
            .split("<br/>")
            .map { it.trim() }
            .map { it.replace(Regex("\\s*\\(.*?\\)\\s*"), "") }
            .map { it.replace(Regex("[0-9.]+$"), "") }
            .map { it.replace("*", "").replace("-", "").replace("+", "") }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(",")

        val calorie = row.calInfo
            .replace(Regex("[^0-9.]"), "")
            .toDoubleOrNull() ?: 0.0

        return ParsedMeal(
            date = date,
            mealType = mealType,
            calorie = calorie,
            menus = menus,
        )
    }
}