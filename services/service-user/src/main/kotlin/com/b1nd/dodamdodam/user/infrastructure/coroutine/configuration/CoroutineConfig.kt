package com.b1nd.dodamdodam.user.infrastructure.coroutine.configuration

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class CoroutineConfig {
    @Bean(destroyMethod = "shutdown")
    fun coroutineExecutor(): ExecutorService =
        Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
        )

    @Bean
    fun blockingExecutor(executor: ExecutorService) =
        CoroutineBlockingExecutor(executor.asCoroutineDispatcher())
}