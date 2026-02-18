package com.b1nd.dodamdodam.core.common.coroutine

interface BlockingExecutor {
    suspend fun <T> execute(block: () -> T): T
}