package com.b1nd.dodamdodam.user.infrastructure.grpc.configuration

import com.b1nd.dodamdodam.grpc.exception.handler.GrpcExceptionHandler
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfig {
    @Bean
    @GrpcGlobalServerInterceptor
    fun grpcExceptionHandler() = GrpcExceptionHandler()
}