package com.b1nd.dodamdodam.core.common.response

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val code: String? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(success = true, data = data)
        }

        fun <T> error(message: String, code: String): ApiResponse<T> {
            return ApiResponse(success = false, message = message, code = code)
        }
    }
}
