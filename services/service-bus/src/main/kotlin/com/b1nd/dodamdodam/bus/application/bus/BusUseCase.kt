package com.b1nd.dodamdodam.bus.application.bus

import com.b1nd.dodamdodam.bus.application.bus.data.request.BusApplicantRequest
import com.b1nd.dodamdodam.bus.application.bus.data.request.BusBoardRequest
import com.b1nd.dodamdodam.bus.application.bus.data.request.BusRequest
import com.b1nd.dodamdodam.bus.application.bus.data.response.BusApplicantResponse
import com.b1nd.dodamdodam.bus.application.bus.data.response.BusDetailResponse
import com.b1nd.dodamdodam.bus.application.bus.data.response.BusResponse
import com.b1nd.dodamdodam.bus.application.bus.data.response.MemberWithBusApplicantResponse
import com.b1nd.dodamdodam.bus.domain.bus.entity.BusApplicantEntity
import com.b1nd.dodamdodam.bus.domain.bus.enumeration.BoardingType
import com.b1nd.dodamdodam.bus.domain.bus.service.BusApplicantService
import com.b1nd.dodamdodam.bus.domain.bus.service.BusService
import com.b1nd.dodamdodam.bus.infrastructure.user.client.UserClient
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class BusUseCase(
    private val busService: BusService,
    private val busApplicantService: BusApplicantService,
    private val userClient: UserClient
) {
    fun createBus(request: BusRequest): Response<Unit> {
        busService.save(request.toEntity())
        return Response.created("버스 생성 성공")
    }

    fun createBusApplicant(request: BusApplicantRequest): Response<Unit> {
        val bus = busService.getById(request.busId)
        val applicants = request.memberIds.map { memberId ->
            BusApplicantEntity(
                memberId = memberId,
                bus = bus,
                boardingType = BoardingType.BEFORE_BOARDING
            )
        }
        busApplicantService.saveAll(applicants)
        return Response.created("버스 탑승자 생성 성공")
    }

    @Transactional(readOnly = true)
    fun getBuses(): Response<List<BusResponse>> {
        val buses = busService.getAll().map { BusResponse.of(it) }
        return Response.ok("버스 조회 성공", buses)
    }

    @Transactional(readOnly = true)
    fun busDetail(id: Long): Response<BusDetailResponse> {
        val bus = busService.getById(id)
        val applicants = busApplicantService.getByBus(bus)
        val memberInfoMap = fetchMemberInfoMap(applicants)

        val users = applicants.map { applicant ->
            val info = memberInfoMap[applicant.memberId]
            MemberWithBusApplicantResponse.of(
                applicant = applicant,
                memberName = info?.name,
                profileImage = info?.profileImage?.ifEmpty { null }
            )
        }
        return Response.ok("버스 상세 조회 성공", BusDetailResponse.of(bus.id!!, bus.name, users))
    }

    @Transactional(readOnly = true)
    fun getRequestedSeats(id: Long): Response<List<Int?>> {
        val bus = busService.getById(id)
        val seats = busApplicantService.getByBus(bus).map { it.seat }
        return Response.ok("버스 좌석 조회 성공", seats)
    }

    fun apply(seat: Int): Response<Unit> {
        val memberId = getCurrentUsername()
        val applicant = busApplicantService.getByMemberId(memberId)
        busApplicantService.checkSeatAvailable(applicant.bus, seat)
        applicant.updateSeat(seat)
        applicant.updateBoardingType(BoardingType.BEFORE_BOARDING)
        busApplicantService.save(applicant)
        return Response.created("버스 신청 성공")
    }

    fun changeBoardingType(request: BusBoardRequest): Response<Unit> {
        val memberId = getCurrentUsername()
        val applicant = busApplicantService.getByMemberId(memberId)
        applicant.updateBoardingType(request.boardingType)
        busApplicantService.save(applicant)
        return Response.ok("탑승정보 수정 성공")
    }

    fun changeSeat(seat: Int): Response<Unit> {
        val memberId = getCurrentUsername()
        val applicant = busApplicantService.getByMemberId(memberId)
        busApplicantService.checkSeatAvailable(applicant.bus, seat)
        applicant.updateSeat(seat)
        busApplicantService.save(applicant)
        return Response.ok("버스 좌석 변경 성공")
    }

    fun updateBus(id: Long, request: BusRequest): Response<Unit> {
        val bus = busService.getById(id)
        bus.modifyName(request.name)
        busService.save(bus)
        return Response.ok("버스 정보 수정 성공")
    }

    fun deleteBus(id: Long): Response<Unit> {
        busApplicantService.deleteByBusId(id)
        busService.deleteById(id)
        return Response.noContent("버스 삭제 성공")
    }

    @Transactional(readOnly = true)
    fun my(): Response<BusApplicantResponse?> {
        val memberId = getCurrentUsername()
        val applicant = busApplicantService.getByMemberIdOrNull(memberId)
        return Response.ok("내 버스 신청 정보 조회 성공", BusApplicantResponse.of(applicant))
    }

    private fun fetchMemberInfoMap(
        applicants: List<BusApplicantEntity>
    ): Map<String, com.b1nd.dodamdodam.grpc.user.UserInfoResponse> {
        val memberIds = applicants.map { it.memberId }.distinct()
        if (memberIds.isEmpty()) return emptyMap()
        return runBlocking { userClient.getUserInfosByUsernames(memberIds) }
    }

    private fun getCurrentUsername(): String {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as PassportUserDetails
        return userDetails.username
    }
}
