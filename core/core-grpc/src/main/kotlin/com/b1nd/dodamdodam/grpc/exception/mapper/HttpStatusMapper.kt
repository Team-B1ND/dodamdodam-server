package com.b1nd.dodamdodam.grpc.exception.mapper

import io.grpc.Status
import org.springframework.http.HttpStatus

fun httpStatusToGrpcStatus(httpStatus: HttpStatus): Status =
    when (httpStatus) {
        HttpStatus.BAD_REQUEST -> Status.INVALID_ARGUMENT
        HttpStatus.UNAUTHORIZED -> Status.UNAUTHENTICATED
        HttpStatus.FORBIDDEN -> Status.PERMISSION_DENIED
        HttpStatus.NOT_FOUND -> Status.NOT_FOUND
        else -> Status.INTERNAL
    }