package com.b1nd.dodamdodam.bus.presentation.bus

import com.b1nd.dodamdodam.bus.application.bus.BusUseCase
import com.b1nd.dodamdodam.bus.application.bus.data.request.BusApplicantRequest
import com.b1nd.dodamdodam.bus.application.bus.data.request.BusBoardRequest
import com.b1nd.dodamdodam.bus.application.bus.data.request.BusRequest
import com.b1nd.dodamdodam.bus.application.bus.data.response.BusApplicantResponse
import com.b1nd.dodamdodam.bus.application.bus.data.response.BusDetailResponse
import com.b1nd.dodamdodam.bus.application.bus.data.response.BusResponse
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BusController(
    private val busUseCase: BusUseCase
) {
    @GetMapping("/bus")
    fun getBuses(): Response<List<BusResponse>> {
        return busUseCase.getBuses()
    }

    @GetMapping("/bus/{id}/seats")
    fun getRequestedSeats(@PathVariable id: Long): Response<List<Int?>> {
        return busUseCase.getRequestedSeats(id)
    }

    @GetMapping("/bus/{id}")
    fun busDetail(@PathVariable id: Long): Response<BusDetailResponse> {
        return busUseCase.busDetail(id)
    }

    @UserAccess
    @GetMapping("/bus/my")
    fun getMy(): Response<BusApplicantResponse?> {
        return busUseCase.my()
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @PostMapping("/bus")
    fun createBus(@RequestBody request: BusRequest): Response<Unit> {
        return busUseCase.createBus(request)
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @PostMapping("/bus/board")
    fun createBusApplicant(@RequestBody request: BusApplicantRequest): Response<Unit> {
        return busUseCase.createBusApplicant(request)
    }

    @UserAccess
    @PostMapping("/bus/board/{seat}")
    fun apply(@PathVariable seat: Int): Response<Unit> {
        return busUseCase.apply(seat)
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @PutMapping("/bus/{id}")
    fun updateBus(@PathVariable id: Long, @RequestBody request: BusRequest): Response<Unit> {
        return busUseCase.updateBus(id, request)
    }

    @UserAccess
    @PatchMapping("/bus/board/{seat}")
    fun changeSeat(@PathVariable seat: Int): Response<Unit> {
        return busUseCase.changeSeat(seat)
    }

    @UserAccess
    @PatchMapping("/bus/board")
    fun changeBoard(@RequestBody request: BusBoardRequest): Response<Unit> {
        return busUseCase.changeBoardingType(request)
    }

    @UserAccess(roles = [RoleType.TEACHER, RoleType.ADMIN])
    @DeleteMapping("/bus/{id}")
    fun deleteBus(@PathVariable id: Long): Response<Unit> {
        return busUseCase.deleteBus(id)
    }
}
