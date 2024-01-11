package b1nd.dodamapi.bus;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.bus.application.BusService;
import b1nd.dodamcore.bus.application.dto.req.BusReq;
import b1nd.dodamcore.bus.application.dto.res.BusRes;
import b1nd.dodamcore.bus.domain.entity.Bus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/bus")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping
    public ResponseData<List<Bus>> getValidBuses() {
        List<Bus> buses = busService.getValidBuses();
        return ResponseData.ok("유효 버스 조회 성공", buses);
    }

    @GetMapping("/list")
    public ResponseData<List<BusRes>> getBuses(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "limit") int limit
    ) {
        List<BusRes> buses = busService.getBuses(page, limit);
        return ResponseData.ok("버스 조회 성공", buses);
    }

    @GetMapping("/date")
    public ResponseData<List<BusRes>> getBusesByDate(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("day") int day
    ) {
        List<BusRes> buses = busService.getBusesByDate(year, month, day);
        return ResponseData.ok("해당 날짜의 버스 조회 성공", buses);
    }

    @GetMapping(value = "/apply")
    public ResponseData<Bus> getAppliedBus() {
        Bus bus = busService.getAppliedBus();
        return ResponseData.ok("신청한 버스 조회 성공", bus);
    }

    @PostMapping
    public Response createBus(@RequestBody @Valid BusReq createBusReq) {
        busService.createBus(createBusReq);
        return Response.created("버스 생성 성공");
    }

    @PatchMapping("/{id}")
    public Response modifyBus(
            @PathVariable int id,
            @RequestBody BusReq modifyBusReq
    ) {
        busService.modifyBus(id, modifyBusReq);
        return Response.ok("버스 수정 성공");
    }

    @DeleteMapping("/{id}")
    public Response deleteBus(@PathVariable int id) {
        busService.deleteBus(id);
        return Response.ok("버스 삭제 성공");
    }

    @PostMapping("/apply/{id}")
    public Response applyBus(@PathVariable int id) {
        busService.applyBus(id);
        return Response.created("버스 신청 성공");
    }

    @PatchMapping("/apply/{id}")
    public Response modifyAppliedBus(@PathVariable int id) {
        busService.modifyAppliedBus(id);
        return Response.ok("버스 신청 수정 성공");
    }

    @DeleteMapping(value = "/apply/{id}")
    public Response cancelAppliedBus(@PathVariable int id) {
        busService.cancelAppliedBus(id);
        return Response.ok("버스 탑승 취소 성공");
    }
}
