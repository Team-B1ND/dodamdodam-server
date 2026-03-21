package com.b1nd.dodamdodam.user.application.openapi.data.request

import java.util.UUID

data class GetUsersByPublicIdsRequest(
    val userIds: List<UUID>
)