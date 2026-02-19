package com.b1nd.dodamdodam.core.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CoroutineBlockingExecutor(
    private val dispatcher: CoroutineDispatcher
): BlockingExecutor {
    override suspend fun <T> execute(block: () -> T) =
        withContext(dispatcher) { block() }
}