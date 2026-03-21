package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)
