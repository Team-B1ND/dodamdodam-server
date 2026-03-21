package com.b1nd.dodamdodam.user.presentation.user.grpc

import com.b1nd.dodamdodam.core.common.coroutine.CoroutineBlockingExecutor
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.grpc.user.GetAllStudentsRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfoRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosByIdsRequest
import com.b1nd.dodamdodam.grpc.user.GetUserInfosReply
import com.b1nd.dodamdodam.grpc.user.UserInfoMessage
import com.b1nd.dodamdodam.grpc.user.UserInfoReply
import com.b1nd.dodamdodam.grpc.user.UserInfoServiceGrpcKt
import com.b1nd.dodamdodam.user.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.user.domain.student.service.StudentService
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.service.UserService
import net.devh.boot.grpc.server.service.GrpcService
import java.util.UUID

@GrpcService
class UserInfoGrpcController(
    private val userService: UserService,
    private val studentService: StudentService,
    private val blockingExecutor: CoroutineBlockingExecutor
) : UserInfoServiceGrpcKt.UserInfoServiceCoroutineImplBase() {

    override suspend fun getUserInfo(request: GetUserInfoRequest): UserInfoReply {
        val userInfo = blockingExecutor.execute {
            val user = userService.get(UUID.fromString(request.publicId))
            val roles = userService.getRoles(user)
            val student = studentService.getOrNull(user)
            buildUserInfoMessage(user, roles, student)
        }
        return UserInfoReply.newBuilder().setUser(userInfo).build()
    }

    override suspend fun getUserInfosByIds(request: GetUserInfosByIdsRequest): GetUserInfosReply {
        val userInfos = blockingExecutor.execute {
            val publicIds = request.publicIdsList.map { UUID.fromString(it) }
            val users = userService.getByPublicIds(publicIds)
            val rolesMap = userService.getRolesMap(users)
            val studentMap = studentService.getByUsers(users)

            users.map { user ->
                buildUserInfoMessage(user, rolesMap[user] ?: emptySet(), studentMap[user])
            }
        }
        return GetUserInfosReply.newBuilder().addAllUsers(userInfos).build()
    }

    override suspend fun getAllStudents(request: GetAllStudentsRequest): GetUserInfosReply {
        val userInfos = blockingExecutor.execute {
            val students = studentService.getAll()
            val users = students.map { it.user }
            val rolesMap = userService.getRolesMap(users)

            students.map { student ->
                buildUserInfoMessage(student.user, rolesMap[student.user] ?: emptySet(), student)
            }
        }
        return GetUserInfosReply.newBuilder().addAllUsers(userInfos).build()
    }

    private fun buildUserInfoMessage(
        user: UserEntity,
        roles: Set<RoleType>,
        student: StudentEntity?
    ): UserInfoMessage {
        val builder = UserInfoMessage.newBuilder()
            .setPublicId(user.publicId.toString())
            .setName(user.name)
            .addAllRoles(roles.map { it.name })
        student?.let {
            builder.setGrade(it.grade)
            builder.setRoom(it.room)
            builder.setNumber(it.number)
        }
        return builder.build()
    }
}
