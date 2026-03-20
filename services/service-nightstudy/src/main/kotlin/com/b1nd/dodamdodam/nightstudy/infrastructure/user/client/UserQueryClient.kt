package com.b1nd.dodamdodam.nightstudy.infrastructure.user.client

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.grpc.user.GetUsersRequest
import com.b1nd.dodamdodam.grpc.user.GetUsersResponse
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class UserQueryClient {
    @GrpcClient("service-user")
    private lateinit var stub: UserQueryServiceGrpcKt.UserQueryServiceCoroutineStub

    suspend fun getUsers(publicIds: List<String>): GetUsersResponse = runCatching {
        val request = GetUsersRequest.newBuilder()
            .addAllPublicIds(publicIds)
            .build()

        stub.getUsers(request)
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
