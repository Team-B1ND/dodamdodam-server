package b1nd.dodam.restapi.outsleeping.presentation;

import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;
import b1nd.dodam.restapi.outsleeping.application.OutSleepingUseCase;
import b1nd.dodam.restapi.outsleeping.application.data.req.ApplyOutSleepingReq;
import b1nd.dodam.restapi.outsleeping.application.data.req.RejectOutSleepingReq;
import b1nd.dodam.restapi.outsleeping.application.data.res.OutSleepingRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
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
    public ResponseData<List<OutSleepingRes>> getBy(@RequestParam LocalDate endAt){
        return useCase.getByEndAt(endAt);
    }

    @GetMapping("/my")
    public ResponseData<List<OutSleepingRes>> getMy() {
        return useCase.getMy();
    }

    @GetMapping("/valid")
    public ResponseData<List<OutSleepingRes>> getValid() {
        return useCase.getValid();
    }

    @GetMapping("/residual")
    public ResponseData<List<MemberInfoRes>> getResidual() {
        return useCase.getRemainder();
    }

}
