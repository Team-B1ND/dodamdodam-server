package b1nd.dodam.restapi.bus.presentation;

import b1nd.dodam.restapi.bus.application.BusUseCase;
import b1nd.dodam.restapi.bus.application.data.req.BusApplicantReq;
import b1nd.dodam.restapi.bus.application.data.req.BusBoardReq;
import b1nd.dodam.restapi.bus.application.data.req.BusReq;
import b1nd.dodam.restapi.bus.application.data.res.BusApplicantRes;
import b1nd.dodam.restapi.bus.application.data.res.BusDetailRes;
import b1nd.dodam.restapi.bus.application.data.res.BusRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
class BusController {
    private final BusUseCase useCase;

    @GetMapping
    public ResponseData<List<BusRes>> buses() {
        return useCase.getBus();
    }

    @GetMapping("/{id}/seats")
    public ResponseData<List<Integer>> requestedSeats(@PathVariable long id) {
        return useCase.getRequestedSeats(id);
    }

    @GetMapping("/{id}")
    public ResponseData<BusDetailRes> busDetail(@PathVariable long id) {
        return useCase.busDetail(id);
    }

    @GetMapping("/my")
    public ResponseData<BusApplicantRes> getMy() {
        return useCase.my();
    }

    @PostMapping
    public Response createBus(@RequestBody BusReq req) {
        return useCase.createBus(req);
    }

    @PostMapping("/board")
    public Response createBusApplicationStudent(@RequestBody BusApplicantReq req) {
        return useCase.createBusApplicant(req);
    }

    @PostMapping("/board/{seat}")
    public Response applyBus(@PathVariable int seat) {
        return useCase.apply(seat);
    }

    @PutMapping("/{id}")
    public Response modifyBus(@PathVariable long id, @RequestBody BusReq req) {
        return useCase.updateBus(id, req);
    }

    @PatchMapping("board/{seat}")
    public Response changeSeat(@PathVariable int seat) {
        return useCase.changeSeat(seat);
    }

    @PatchMapping("/board")
    public Response changeBoard(@RequestBody BusBoardReq req) {
        return useCase.changeBoardingType(req.boardingType());
    }

    @DeleteMapping("/{id}")
    public Response deleteBus(@PathVariable long id) {
        return useCase.deleteBus(id);
    }
}
