package b1nd.dodamapi.outsleeping.handler;

import b1nd.dodamcore.common.response.Response;
import b1nd.dodamcore.common.response.ResponseData;
import b1nd.dodamapi.outsleeping.usecase.OutSleepingUseCase;
import b1nd.dodamapi.outsleeping.usecase.dto.req.ApplyOutSleepingReq;
import b1nd.dodamapi.outsleeping.usecase.dto.req.RejectOutSleepingReq;
import b1nd.dodamapi.outsleeping.usecase.dto.res.OutSleepingRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/out-sleeping")
@RequiredArgsConstructor
public class OutSleepingController {

    private final OutSleepingUseCase useCase;

    @PostMapping
    public Response apply(@RequestBody @Valid ApplyOutSleepingReq req) {
        return useCase.apply(req);
    }

    @PatchMapping("/{id}/allow")
    public Response allow(@PathVariable Long id) {
        return useCase.allow(id);
    }

    @PatchMapping("/{id}/reject")
    public Response reject(@PathVariable Long id, @RequestBody Optional<RejectOutSleepingReq> req) {
        return useCase.reject(id, req);
    }

    @PatchMapping("/{id}/revert")
    public Response revert(@PathVariable Long id) {
        return useCase.revert(id);
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable Long id) {
        return useCase.cancel(id);
    }

    @GetMapping
    public ResponseData<List<OutSleepingRes>> getBy(@RequestParam LocalDate date) {
        return useCase.getByDate(date);
    }

    @GetMapping("/my")
    public ResponseData<List<OutSleepingRes>> getMy() {
        return useCase.getMy();
    }

    @GetMapping("/valid")
    public ResponseData<List<OutSleepingRes>> getValid() {
        return useCase.getValid();
    }

}
