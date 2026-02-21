package com.b1nd.dodamdodam.bus.infrastructure.user.mapper

import com.b1nd.dodamdodam.grpc.user.UserInfoResponse
import org.springframework.stereotype.Component

@Component
class UserInfoMapper {

    fun toMemberName(userInfo: UserInfoResponse): String {
        return userInfo.name
    }

    fun toProfileImage(userInfo: UserInfoResponse): String? {
        return userInfo.profileImage.ifEmpty { null }
    }
}
