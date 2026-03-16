package com.b1nd.dodamdodam.inapp.application.app.data.response

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content: List<T>,
    val hasNext: Boolean,
) {
    companion object {
        fun <T> of(page: Page<T>): PageResponse<T> =
            PageResponse(content = page.content, hasNext = page.hasNext())
    }
}
