package com.b1nd.dodamdodam.notice.infrastructure.user.mapper

import com.b1nd.dodamdodam.grpc.user.UserInfoResponse
import com.b1nd.dodamdodam.notice.application.notice.data.response.MemberInfoResponse
import org.springframework.stereotype.Component

@Component
class UserInfoMapper {

    fun toMemberInfoResponse(userInfo: UserInfoResponse): MemberInfoResponse {
        return MemberInfoResponse(
            id = userInfo.userId,
            name = userInfo.name,
            profileImage = userInfo.profileImage.ifEmpty { null }
        )
    }
}
