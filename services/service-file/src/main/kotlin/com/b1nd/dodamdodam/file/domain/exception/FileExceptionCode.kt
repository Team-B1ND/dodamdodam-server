package com.b1nd.dodamdodam.file.domain.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class FileExceptionCode(
    override val status: HttpStatus,
    override val message: String
): ExceptionCode {
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "파일이 비어있어요."),
    FILE_TYPE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 형식이에요."),
    FILE_DIMENSION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 크기(가로x세로)예요."),
    FILE_DIMENSION_READ_FAILED(HttpStatus.BAD_REQUEST, "파일의 크기(가로x세로)를 읽을 수 없어요."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했어요."),
}
