package com.b1nd.dodamdodam.neis.infrastructure.user

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.grpc.user.GetUserRequest
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import com.b1nd.dodamdodam.grpc.user.UserResponse
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class UserQueryClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserQueryServiceGrpcKt.UserQueryServiceCoroutineStub

    suspend fun getUser(publicId: String): UserResponse = runCatching {
        val request = GetUserRequest.newBuilder().setPublicId(publicId).build()
        stub.getUser(request)
    }.getOrElse { ex ->
        when (ex) {
            is StatusException -> throw when (ex.status.code) {
                Status.Code.INVALID_ARGUMENT -> IllegalArgumentException(ex.message)
                else -> BaseInternalServerException()
            }
            is StatusRuntimeException -> throw when (ex.status.code) {
                Status.Code.INVALID_ARGUMENT -> IllegalArgumentException(ex.message)
                else -> BaseInternalServerException()
            }
            else -> throw ex
        }
    }
}