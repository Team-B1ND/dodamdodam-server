package com.b1nd.dodamdodam.auth.infrastructure.user.client

import com.b1nd.dodamdodam.auth.domain.principal.exception.PasswordIncorrectException
import com.b1nd.dodamdodam.auth.domain.principal.exception.UserNotFoundException
import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.grpc.user.UserCredentialServiceGrpcKt
import com.b1nd.dodamdodam.grpc.user.VerifyPasswordRequest
import io.grpc.Status
import io.grpc.StatusException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class UserClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserCredentialServiceGrpcKt.UserCredentialServiceCoroutineStub

    suspend fun verifyPassword(username: String, password: String): Result<Boolean> = runCatching {
        val request = VerifyPasswordRequest.newBuilder()
            .setUsername(username)
            .setRawPassword(password)
            .build()

        stub.verifyPassword(request).valid
    }.onFailure { ex ->
        if (ex is StatusException) {
            throw when (ex.status.code) {
                Status.Code.UNAUTHENTICATED -> PasswordIncorrectException()
                Status.Code.NOT_FOUND -> UserNotFoundException()
                else -> BaseInternalServerException()
            }
        }
    }
}