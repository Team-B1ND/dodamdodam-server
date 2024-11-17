package b1nd.dodam.restapi.bus.presentation;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.restapi.bus.application.BusApplicationUseCase;
import b1nd.dodam.restapi.bus.application.BusUseCase;
import b1nd.dodam.restapi.bus.application.data.req.BusReq;
import b1nd.dodam.restapi.bus.application.data.res.BusRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
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
    public Response register(@RequestBody @Valid BusReq req) {
        return busUseCase.register(req);
    }

    @PatchMapping("/{id}")
    public Response modify(@PathVariable int id, @RequestBody BusReq req) {
        return busUseCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable int id) {
        return busUseCase.delete(id);
    }

    @GetMapping
    public ResponseData<List<Bus>> getValid() {
        return busUseCase.getValid();
    }

    @GetMapping("/list")
    public ResponseData<List<BusRes>> getAll(@RequestParam int page, @RequestParam int limit) {
        return busUseCase.getAll(page, limit);
    }

    @GetMapping("/date")
    public ResponseData<List<BusRes>> getByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        return busUseCase.getByDate(year, month, day);
    }

    @GetMapping("/apply")
    public ResponseData<Bus> getMy() {
        return busUseCase.getMy();
    }

    @PostMapping("/apply/{id}")
    public Response apply(@PathVariable int id) {
        return busApplicationUseCase.apply(id);
    }

    @PatchMapping("/apply/{id}")
    public Response modifyApplication(@PathVariable int id) {
        return busApplicationUseCase.modify(id);
    }

    @DeleteMapping("/apply")
    public Response cancelApplication() {
        return busApplicationUseCase.cancel();
    }

}
