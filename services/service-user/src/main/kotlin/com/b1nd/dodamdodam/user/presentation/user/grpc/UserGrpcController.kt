package com.b1nd.dodamdodam.user.presentation.user.grpc

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import com.b1nd.dodamdodam.grpc.user.UserCredentialServiceGrpcKt
import com.b1nd.dodamdodam.grpc.user.VerifyPasswordRequest as VerifyPasswordGrpcRequest
import com.b1nd.dodamdodam.grpc.user.VerifyPasswordResponse
import com.b1nd.dodamdodam.user.application.user.UserUseCase
import com.b1nd.dodamdodam.user.application.user.data.request.VerifyPasswordRequest
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class UserGrpcController(
    private val useCase: UserUseCase,
    private val blockingExecutor: CoroutineBlockingExecutor
): UserCredentialServiceGrpcKt.UserCredentialServiceCoroutineImplBase() {
    override suspend fun verifyPassword(request: VerifyPasswordGrpcRequest): VerifyPasswordResponse {
        val isValid: Boolean = blockingExecutor.execute {
            useCase.verifyPassword(VerifyPasswordRequest(
                username = request.username,
                password = request.rawPassword
            ))
        }
        return VerifyPasswordResponse.newBuilder().setValid(isValid).build()
    }
}