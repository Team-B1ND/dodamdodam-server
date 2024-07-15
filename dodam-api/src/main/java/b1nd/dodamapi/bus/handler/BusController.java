package b1nd.dodamapi.bus.handler;

import b1nd.dodamapi.bus.usecase.BusApplicationUseCase;
import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamapi.bus.usecase.BusUseCase;
import b1nd.dodamapi.bus.usecase.dto.req.BusReq;
import b1nd.dodamapi.bus.usecase.dto.res.BusRes;
import b1nd.dodamcore.bus.domain.entity.Bus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
public class BusController {

    private final BusUseCase busUseCase;
    private final BusApplicationUseCase busApplicationUseCase;

    @PostMapping
    public Response registerBus(@RequestBody @Valid BusReq req) {
        return busUseCase.register(req);
    }

    @PatchMapping("/{id}")
    public Response modifyBus(@PathVariable int id, @RequestBody BusReq req) {
        return busUseCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response deleteBus(@PathVariable int id) {
        return busUseCase.delete(id);
    }

    @GetMapping
    public ResponseData<List<Bus>> getValidBuses() {
        return busUseCase.getValid();
    }

    @GetMapping("/list")
    public ResponseData<List<BusRes>> getBuses(@RequestParam int page, @RequestParam int limit) {
        return busUseCase.getBuses(page, limit);
    }

    @GetMapping("/date")
    public ResponseData<List<BusRes>> getBusesByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        return busUseCase.getBusesByDate(year, month, day);
    }

    @GetMapping("/apply")
    public ResponseData<Bus> getMy() {
        return busUseCase.getMy();
    }

    @PostMapping("/apply/{id}")
    public Response applyBus(@PathVariable int id) {
        return busApplicationUseCase.apply(id);
    }

    @PatchMapping("/apply/{id}")
    public Response modifyApplication(@PathVariable int id) {
        return busApplicationUseCase.modify(id);
    }

    @DeleteMapping("/apply/{id}")
    public Response cancelApplication(@PathVariable int id) {
        return busApplicationUseCase.cancel(id);
    }
}
