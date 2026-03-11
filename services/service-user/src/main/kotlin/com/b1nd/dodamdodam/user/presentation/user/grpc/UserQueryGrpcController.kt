package com.b1nd.dodamdodam.user.presentation.user.grpc

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import com.b1nd.dodamdodam.grpc.user.GetUserRequest
import com.b1nd.dodamdodam.grpc.user.GetUsersRequest
import com.b1nd.dodamdodam.grpc.user.GetUsersResponse
import com.b1nd.dodamdodam.grpc.user.UserQueryServiceGrpcKt
import com.b1nd.dodamdodam.grpc.user.UserResponse
import com.b1nd.dodamdodam.user.application.openapi.OpenApiUserUseCase
import com.b1nd.dodamdodam.user.application.openapi.data.toGetUsersGrpcResponse
import com.b1nd.dodamdodam.user.application.openapi.data.toGrpcResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.server.service.GrpcService
import java.util.UUID

@GrpcService
class UserQueryGrpcController(
    private val openApiUserUseCase: OpenApiUserUseCase,
    private val blockingExecutor: CoroutineBlockingExecutor,
) : UserQueryServiceGrpcKt.UserQueryServiceCoroutineImplBase() {

    override suspend fun getUser(request: GetUserRequest): UserResponse {
        val user = blockingExecutor.execute {
            openApiUserUseCase.getUser(UUID.fromString(request.publicId))
        }
        return user?.toGrpcResponse()
            ?: throw StatusRuntimeException(
                Status.NOT_FOUND.withDescription("유저를 찾을 수 없어요.")
            )
    }

    override suspend fun getUsers(request: GetUsersRequest): GetUsersResponse {
        val publicIds = request.publicIdsList.map { UUID.fromString(it) }
        val users = blockingExecutor.execute {
            openApiUserUseCase.getUsers(publicIds)
        }
        return users.toGetUsersGrpcResponse()
    }
}