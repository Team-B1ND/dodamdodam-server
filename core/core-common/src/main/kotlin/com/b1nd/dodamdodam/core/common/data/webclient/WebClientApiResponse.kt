package com.b1nd.dodamdodam.core.common.data.webclient

data class WebClientApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null,
    val code: String? = null
) {
    fun isSuccess() = status in 200..299
    fun is4xxError() = status in 400..499
    fun is5xxError() = status in 500..599
    fun isError() = !isSuccess()
}