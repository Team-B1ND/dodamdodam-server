package com.b1nd.dodamdodam.auth.infrastructure.openapi.client

import com.b1nd.dodamdodam.core.common.exception.base.BaseInternalServerException
import com.b1nd.dodamdodam.grpc.inapp.AppApiKeyServiceGrpcKt
import com.b1nd.dodamdodam.grpc.inapp.VerifyApiKeyRequest
import io.grpc.StatusException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class InAppClient {
    @GrpcClient("service-inapp")
    private lateinit var stub: AppApiKeyServiceGrpcKt.AppApiKeyServiceCoroutineStub

    suspend fun verifyApiKey(appPublicId: String, apiKey: String): Boolean = runCatching {
        val request = VerifyApiKeyRequest.newBuilder()
            .setAppPublicId(appPublicId)
            .setApiKey(apiKey)
            .build()

        stub.verifyApiKey(request).valid
    }.getOrElse { ex ->
        if (ex is StatusException) {
            throw BaseInternalServerException()
        }
        throw ex
    }
}