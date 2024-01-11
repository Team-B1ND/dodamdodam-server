package b1nd.dodamapi.bus;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamcore.bus.application.BusService;
import b1nd.dodamcore.bus.application.dto.req.BusReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/bus")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @PostMapping //teacher
    public Response createBus(@RequestBody @Valid BusReq createBusReq) {
        busService.createBus(createBusReq);
        return Response.created("버스 생성 성공");
    }

    @PatchMapping("/{id}") //teacher
    public Response modifyBus(
            @PathVariable int id,
            @RequestBody BusReq modifyBusReq
    ) {
        busService.modifyBus(id, modifyBusReq);
        return Response.ok("버스 수정 성공");
    }

    @DeleteMapping("/{id}") //teacher
    public Response deleteBus(@PathVariable int id) {
        busService.deleteBus(id);
        return Response.ok("버스 삭제 성공");
    }

    @PostMapping("/apply/{id}") //student
    public Response applyBus(@PathVariable int id) {
        busService.applyBus(id);
        return Response.created("버스 신청 성공");
    }

    @PatchMapping("/apply/{id}") //student
    public Response modifyAppliedBus(@PathVariable int id) {
        busService.modifyAppliedBus(id);
        return Response.ok("버스 신청 수정 성공");
    }

    @DeleteMapping(value = "/apply/{id}") //student
    public Response cancelAppliedBus(@PathVariable int id) {
        busService.cancelAppliedBus(id);
        return Response.ok("버스 탑승 취소 성공");
    }
}
