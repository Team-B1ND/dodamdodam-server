package b1nd.dodamapi.outgoing.handler;

import b1nd.dodamcore.common.response.Response;
import b1nd.dodamcore.common.response.ResponseData;
import b1nd.dodamapi.outgoing.usecase.OutGoingUseCase;
import b1nd.dodamapi.outgoing.usecase.dto.req.RejectOutGoingReq;
import b1nd.dodamapi.outgoing.usecase.dto.res.OutGoingRes;
import b1nd.dodamapi.outgoing.usecase.dto.req.ApplyOutGoingReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/out-going")
@RequiredArgsConstructor
public class OutGoingController {

    private final OutGoingUseCase useCase;

    @PostMapping
    public Response apply(@RequestBody @Valid ApplyOutGoingReq req) {
        return useCase.apply(req);
    }

    @PatchMapping("/{id}/allow")
    public Response allow(@PathVariable Long id) {
        return useCase.allow(id);
    }

    @PatchMapping("/{id}/reject")
    public Response reject(@PathVariable Long id, @RequestBody Optional<RejectOutGoingReq> req) {
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
    public ResponseData<List<OutGoingRes>> getByDate(@RequestParam LocalDate date) {
        return useCase.getByDate(date);
    }

    @GetMapping("/my")
    public ResponseData<List<OutGoingRes>> getMy() {
        return useCase.getMy();
    }

}