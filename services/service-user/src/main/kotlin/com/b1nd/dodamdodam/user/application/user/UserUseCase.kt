package com.b1nd.dodamdodam.user.application.user

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.producer.KafkaMessageProducer
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.user.application.user.data.request.ChangePasswordRequest
import com.b1nd.dodamdodam.user.application.user.data.request.EnableUserRequest
import com.b1nd.dodamdodam.user.application.user.data.request.StudentRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.TeacherRegisterRequest
import com.b1nd.dodamdodam.user.application.user.data.request.UpdateUserInfoRequest
import com.b1nd.dodamdodam.user.application.user.data.request.VerifyPasswordRequest
import com.b1nd.dodamdodam.user.application.user.data.toUserCreatedEvent
import com.b1nd.dodamdodam.user.application.user.data.toUserUpdatedEvent
import com.b1nd.dodamdodam.user.application.user.data.toStudentEntity
import com.b1nd.dodamdodam.user.application.user.data.toTeacherEntity
import com.b1nd.dodamdodam.user.application.user.data.toUserEntity
import com.b1nd.dodamdodam.user.domain.student.service.StudentService
import com.b1nd.dodamdodam.user.domain.teacher.service.TeacherService
import com.b1nd.dodamdodam.user.domain.user.entity.UserEntity
import com.b1nd.dodamdodam.user.domain.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
@Transactional(rollbackOn = [Exception::class])
class UserUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val teacherService: TeacherService,
    private val kafkaMessageProducer: KafkaMessageProducer
) {
    fun registerStudent(request: StudentRegisterRequest): Response<Any> {
        //TODO 전화번호 검증 로직
        val savedUser: UserEntity = userService.create(
            request.toUserEntity(),
            RoleType.STUDENT
        )
        studentService.create(request.toStudentEntity(savedUser))
        kafkaMessageProducer.send(
            KafkaTopics.USER_CREATED,
            savedUser.toUserCreatedEvent(RoleType.STUDENT)
        )
        return Response.created("학생 계정이 생성되었어요.")
    }

    fun registerTeacher(request: TeacherRegisterRequest): Response<Any> {
        //TODO 전화번호 검증 로직
        val savedUser: UserEntity = userService.create(
            request.toUserEntity(),
            RoleType.TEACHER,
        )
        teacherService.create(request.toTeacherEntity(savedUser))
        kafkaMessageProducer.send(
            KafkaTopics.USER_CREATED,
            savedUser.toUserCreatedEvent(RoleType.TEACHER)
        )

        return Response.created("선생님 계정이 생성되었어요.")
    }

    fun enableUser(request: EnableUserRequest): Response<Any> {
        val user = userService.enable(request.userId)
        val userRoles = userService.getRoles(user)
        kafkaMessageProducer.send(
            KafkaTopics.USER_UPDATED,
            user.toUserUpdatedEvent(userRoles)
        )
        return Response.ok("유저가 활성화되었어요.")
    }

    fun quitUser(): Response<Any> {
        val user = userService.delete(PassportHolder.current().requireUserId())
        val userRoles = userService.getRoles(user)
        kafkaMessageProducer.send(
            KafkaTopics.USER_UPDATED,
            user.toUserUpdatedEvent(userRoles)
        )
        return Response.ok("유저가 탈퇴되었어요.")
    }

    fun updateUser(request: UpdateUserInfoRequest): Response<Any> {
        val passport = PassportHolder.current()
        val userId = passport.requireUserId()
        val updatedUser = userService.update(userId, request.name, request.phone, request.profileImage)
        val roles = userService.getRoles(updatedUser)
        kafkaMessageProducer.send(
            KafkaTopics.USER_UPDATED,
            updatedUser.toUserUpdatedEvent(roles)
        )
        return Response.ok("유저 정보가 변경되었어요.")
    }

    fun changePassword(request: ChangePasswordRequest): Response<Any> {
        val passport = PassportHolder.current()
        val userId = passport.requireUserId()
        userService.updatePassword(userId, request.postPassword, request.newPassword)
        return Response.ok("비밀번호가 변경되었어요.")
    }

    fun verifyPassword(request: VerifyPasswordRequest): Boolean {
        try {
            userService.verify(request.username, request.password)
            return true
        } catch (_: Exception) {
            return false
        }
    }
}
