package com.b1nd.dodamdodam.neis.infrastructure.neis.data

import com.fasterxml.jackson.annotation.JsonProperty

data class NeisMealApiResponse(
    @JsonProperty("mealServiceDietInfo")
    val mealServiceDietInfo: List<MealServiceDietInfo>?,
)

data class MealServiceDietInfo(
    @JsonProperty("head")
    val head: List<Head>?,
    @JsonProperty("row")
    val row: List<NeisMealRow>?,
)

data class Head(
    @JsonProperty("list_total_count")
    val listTotalCount: Int?,
    @JsonProperty("RESULT")
    val result: HeadResult?,
)

data class HeadResult(
    @JsonProperty("CODE")
    val code: String,
    @JsonProperty("MESSAGE")
    val message: String,
)

data class NeisMealRow(
    @JsonProperty("ATPT_OFCDC_SC_CODE")
    val eduOfficeCode: String,
    @JsonProperty("SD_SCHUL_CODE")
    val schoolCode: String,
    @JsonProperty("MLSV_YMD")
    val mealDate: String,
    @JsonProperty("MMEAL_SC_CODE")
    val mealCode: String,
    @JsonProperty("DDISH_NM")
    val dishName: String,
    @JsonProperty("CAL_INFO")
    val calInfo: String,
)