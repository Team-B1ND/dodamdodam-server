package com.b1nd.dodamdodam.outsleeping

import com.b1nd.dodamdodam.core.security.passport.Passport
import com.b1nd.dodamdodam.core.security.passport.crypto.PassportCompressor
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.outsleeping.domain.deadline.entity.OutSleepingDeadlineEntity
import com.b1nd.dodamdodam.outsleeping.domain.deadline.repository.OutSleepingDeadlineRepository
import com.b1nd.dodamdodam.outsleeping.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.outsleeping.domain.member.repository.MemberRepository
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository.OutSleepingRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OutSleepingIntegrationTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper
    @Autowired lateinit var outSleepingRepository: OutSleepingRepository
    @Autowired lateinit var memberRepository: MemberRepository
    @Autowired lateinit var deadlineRepository: OutSleepingDeadlineRepository

    private val studentId = UUID.randomUUID()
    private val teacherId = UUID.randomUUID()
    private val otherStudentId = UUID.randomUUID()

    private fun passportHeader(userId: UUID, role: RoleType): String {
        val passport = Passport(
            userId = userId,
            username = "testuser",
            role = listOf(role),
            enabled = true,
            os = "test",
            version = "1.0",
            issuedAt = System.currentTimeMillis(),
            expiredAt = System.currentTimeMillis() + 3600_000
        )
        return PassportCompressor.compress(passport)
    }

    private val studentPassport get() = passportHeader(studentId, RoleType.STUDENT)
    private val teacherPassport get() = passportHeader(teacherId, RoleType.TEACHER)
    private val otherStudentPassport get() = passportHeader(otherStudentId, RoleType.STUDENT)

    @BeforeEach
    fun setUp() {
        outSleepingRepository.deleteAll()
        memberRepository.deleteAll()
        deadlineRepository.deleteAll()

        // Set deadline to far future so tests can apply
        deadlineRepository.save(
            OutSleepingDeadlineEntity(
                dayOfWeek = DayOfWeek.SUNDAY,
                time = LocalTime.of(23, 59)
            )
        )

        // Create test members
        memberRepository.save(MemberEntity(userId = studentId, name = "학생1", role = "STUDENT", grade = 2, room = 3, number = 15))
        memberRepository.save(MemberEntity(userId = otherStudentId, name = "학생2", role = "STUDENT", grade = 1, room = 2, number = 10))
        memberRepository.save(MemberEntity(userId = teacherId, name = "선생님1", role = "TEACHER"))
    }

    @Nested
    @DisplayName("POST /out-sleeping - 외박 신청")
    inner class Apply {

        @Test
        fun `학생이 외박을 신청할 수 있다`() {
            val body = mapOf("reason" to "가족 행사", "startAt" to "2025-03-22", "endAt" to "2025-03-23")

            mockMvc.perform(
                post("/out-sleeping")
                    .header("X-User-Passport", studentPassport)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").exists())
        }

        @Test
        fun `인증 없이 외박 신청 시 실패한다`() {
            val body = mapOf("reason" to "가족 행사", "startAt" to "2025-03-22", "endAt" to "2025-03-23")

            mockMvc.perform(
                post("/out-sleeping")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
            )
                .andExpect(status().isForbidden)
        }
    }

    @Nested
    @DisplayName("GET /out-sleeping/my - 내 외박 조회")
    inner class GetMy {

        @Test
        fun `학생이 자신의 외박 목록을 조회할 수 있다`() {
            outSleepingRepository.save(OutSleepingEntity(userId = studentId, reason = "사유1", startAt = LocalDate.of(2025, 3, 22), endAt = LocalDate.of(2025, 3, 23)))
            outSleepingRepository.save(OutSleepingEntity(userId = studentId, reason = "사유2", startAt = LocalDate.of(2025, 3, 29), endAt = LocalDate.of(2025, 3, 30)))

            mockMvc.perform(
                get("/out-sleeping/my")
                    .header("X-User-Passport", studentPassport)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].student.name").value("학생1"))
                .andExpect(jsonPath("$.data[0].student.grade").value(2))
        }

        @Test
        fun `교사가 내 외박 조회 시 권한 없음`() {
            mockMvc.perform(
                get("/out-sleeping/my")
                    .header("X-User-Passport", teacherPassport)
            )
                .andExpect(status().isForbidden)
                .andExpect(jsonPath("$.status").value(403))
        }
    }

    @Nested
    @DisplayName("DELETE /out-sleeping/{id} - 외박 취소")
    inner class Cancel {

        @Test
        fun `학생이 자신의 PENDING 외박을 취소할 수 있다`() {
            val outSleeping = outSleepingRepository.save(
                OutSleepingEntity(userId = studentId, reason = "사유", startAt = LocalDate.of(2025, 3, 22), endAt = LocalDate.of(2025, 3, 23))
            )

            mockMvc.perform(
                delete("/out-sleeping/${outSleeping.id}")
                    .header("X-User-Passport", studentPassport)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
        }

        @Test
        fun `다른 학생의 외박은 취소할 수 없다`() {
            val outSleeping = outSleepingRepository.save(
                OutSleepingEntity(userId = studentId, reason = "사유", startAt = LocalDate.of(2025, 3, 22), endAt = LocalDate.of(2025, 3, 23))
            )

            mockMvc.perform(
                delete("/out-sleeping/${outSleeping.id}")
                    .header("X-User-Passport", otherStudentPassport)
            )
                .andExpect(status().isForbidden)
                .andExpect(jsonPath("$.status").value(403))
        }

        @Test
        fun `이미 승인된 외박은 취소할 수 없다`() {
            val outSleeping = outSleepingRepository.save(
                OutSleepingEntity(userId = studentId, reason = "사유", startAt = LocalDate.of(2025, 3, 22), endAt = LocalDate.of(2025, 3, 23), status = OutSleepingStatus.ALLOWED)
            )

            mockMvc.perform(
                delete("/out-sleeping/${outSleeping.id}")
                    .header("X-User-Passport", studentPassport)
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.status").value(400))
        }
    }

    @Nested
    @DisplayName("PATCH /out-sleeping/{id} - 외박 수정")
    inner class Modify {

        @Test
        fun `학생이 자신의 PENDING 외박을 수정할 수 있다`() {
            val outSleeping = outSleepingRepository.save(
                OutSleepingEntity(userId = studentId, reason = "원래 사유", startAt = LocalDate.of(2025, 3, 22), endAt = LocalDate.of(2025, 3, 23))
            )
            val body = mapOf("reason" to "변경된 사유", "startAt" to "2025-03-29", "endAt" to "2025-03-30")

            mockMvc.perform(
                patch("/out-sleeping/${outSleeping.id}")
                    .header("X-User-Passport", studentPassport)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
        }
    }

    @Nested
    @DisplayName("TEACHER 엔드포인트 - 승인/거절/되돌리기/조회")
    inner class TeacherEndpoints {

        private lateinit var pendingOutSleeping: OutSleepingEntity

        @BeforeEach
        fun createOutSleeping() {
            pendingOutSleeping = outSleepingRepository.save(
                OutSleepingEntity(userId = studentId, reason = "사유", startAt = LocalDate.now(), endAt = LocalDate.now().plusDays(1))
            )
        }

        @Test
        fun `교사가 외박을 승인할 수 있다`() {
            mockMvc.perform(
                patch("/out-sleeping/${pendingOutSleeping.id}/allow")
                    .header("X-User-Passport", teacherPassport)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("외박 신청을 승인했어요."))
        }

        @Test
        fun `교사가 외박을 거절할 수 있다`() {
            val body = mapOf("rejectReason" to "사유 부족")

            mockMvc.perform(
                patch("/out-sleeping/${pendingOutSleeping.id}/reject")
                    .header("X-User-Passport", teacherPassport)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("외박 신청을 거절했어요."))
        }

        @Test
        fun `교사가 상태를 되돌릴 수 있다`() {
            pendingOutSleeping.allow()
            outSleepingRepository.save(pendingOutSleeping)

            mockMvc.perform(
                patch("/out-sleeping/${pendingOutSleeping.id}/revert")
                    .header("X-User-Passport", teacherPassport)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
        }

        @Test
        fun `학생은 승인할 수 없다`() {
            mockMvc.perform(
                patch("/out-sleeping/${pendingOutSleeping.id}/allow")
                    .header("X-User-Passport", studentPassport)
            )
                .andExpect(status().isForbidden)
                .andExpect(jsonPath("$.status").value(403))
        }

        @Test
        fun `교사가 날짜별 외박 목록을 페이지네이션으로 조회할 수 있다`() {
            mockMvc.perform(
                get("/out-sleeping")
                    .header("X-User-Passport", teacherPassport)
                    .param("date", LocalDate.now().toString())
                    .param("page", "0")
                    .param("size", "10")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].student.name").value("학생1"))
        }

        @Test
        fun `학생은 날짜별 외박 조회 권한이 없다`() {
            mockMvc.perform(
                get("/out-sleeping")
                    .header("X-User-Passport", studentPassport)
                    .param("date", LocalDate.now().toString())
            )
                .andExpect(status().isForbidden)
                .andExpect(jsonPath("$.status").value(403))
        }
    }

    @Nested
    @DisplayName("GET /out-sleeping/valid - 유효한 외박 조회")
    inner class GetValid {

        @Test
        fun `인증된 사용자가 유효한 외박을 조회할 수 있다`() {
            val allowed = OutSleepingEntity(userId = studentId, reason = "사유", startAt = LocalDate.now(), endAt = LocalDate.now().plusDays(1), status = OutSleepingStatus.ALLOWED)
            val pending = OutSleepingEntity(userId = otherStudentId, reason = "사유2", startAt = LocalDate.now(), endAt = LocalDate.now().plusDays(1))
            outSleepingRepository.saveAll(listOf(allowed, pending))

            mockMvc.perform(
                get("/out-sleeping/valid")
                    .header("X-User-Passport", studentPassport)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].status").value("ALLOWED"))
        }
    }

    @Nested
    @DisplayName("GET /out-sleeping/residual - 잔류학생 조회")
    inner class GetResidual {

        @Test
        fun `교사가 잔류학생을 조회할 수 있다`() {
            // 학생1은 외박 승인됨, 학생2는 잔류
            outSleepingRepository.save(
                OutSleepingEntity(userId = studentId, reason = "사유", startAt = LocalDate.now(), endAt = LocalDate.now().plusDays(1), status = OutSleepingStatus.ALLOWED)
            )

            mockMvc.perform(
                get("/out-sleeping/residual")
                    .header("X-User-Passport", teacherPassport)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("학생2"))
        }
    }

    @Nested
    @DisplayName("마감시간 관리")
    inner class Deadline {

        @Test
        fun `마감시간을 조회할 수 있다`() {
            mockMvc.perform(
                get("/out-sleeping/deadline")
                    .header("X-User-Passport", studentPassport)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.dayOfWeek").value("SUNDAY"))
        }

        @Test
        fun `교사가 마감시간을 수정할 수 있다`() {
            val body = mapOf("dayOfWeek" to "FRIDAY", "time" to "18:00")

            mockMvc.perform(
                patch("/out-sleeping/deadline")
                    .header("X-User-Passport", teacherPassport)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(200))
        }

        @Test
        fun `학생은 마감시간을 수정할 수 없다`() {
            val body = mapOf("dayOfWeek" to "FRIDAY", "time" to "18:00")

            mockMvc.perform(
                patch("/out-sleeping/deadline")
                    .header("X-User-Passport", studentPassport)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
            )
                .andExpect(status().isForbidden)
                .andExpect(jsonPath("$.status").value(403))
        }
    }

    @Nested
    @DisplayName("마감시간 초과 테스트")
    inner class DeadlineExceeded {

        @Test
        fun `마감시간이 지나면 외박 신청이 불가능하다`() {
            // 마감시간을 과거로 설정 (월요일 00:00 → 현재 요일이 월요일 이후면 마감)
            deadlineRepository.deleteAll()
            deadlineRepository.save(
                OutSleepingDeadlineEntity(
                    dayOfWeek = DayOfWeek.MONDAY,
                    time = LocalTime.of(0, 0)
                )
            )

            val body = mapOf("reason" to "늦은 신청", "startAt" to "2025-03-22", "endAt" to "2025-03-23")

            val result = mockMvc.perform(
                post("/out-sleeping")
                    .header("X-User-Passport", studentPassport)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
            )

            // 오늘이 월요일이 아니면 마감 초과, 월요일이면 시간 초과
            result.andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("외박 신청 기간이 지났어요."))
        }
    }
}
