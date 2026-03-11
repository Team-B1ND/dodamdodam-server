package com.b1nd.dodamdodam.inapp.presentation.app.grpc

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import com.b1nd.dodamdodam.grpc.inapp.AppApiKeyServiceGrpcKt
import com.b1nd.dodamdodam.grpc.inapp.VerifyApiKeyRequest
import com.b1nd.dodamdodam.grpc.inapp.VerifyApiKeyResponse
import com.b1nd.dodamdodam.inapp.domain.app.service.AppService
import net.devh.boot.grpc.server.service.GrpcService
import java.util.UUID

@GrpcService
class AppApiKeyGrpcController(
    private val appService: AppService,
    private val blockingExecutor: CoroutineBlockingExecutor
) : AppApiKeyServiceGrpcKt.AppApiKeyServiceCoroutineImplBase() {
    override suspend fun verifyApiKey(request: VerifyApiKeyRequest): VerifyApiKeyResponse {
        val isValid = blockingExecutor.execute {
            appService.verifyApiKey(
                appPublicId = UUID.fromString(request.appPublicId),
                rawApiKey = request.apiKey
            )
        }
        return VerifyApiKeyResponse.newBuilder().setValid(isValid).build()
    }
}