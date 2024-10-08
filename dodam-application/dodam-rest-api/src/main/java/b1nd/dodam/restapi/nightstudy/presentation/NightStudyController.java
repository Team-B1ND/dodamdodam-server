package b1nd.dodam.restapi.nightstudy.presentation;

import b1nd.dodam.restapi.nightstudy.application.NightStudyUseCase;
import b1nd.dodam.restapi.nightstudy.application.data.req.ApplyNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.RejectNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.res.NightStudyRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/night-study")
@RequiredArgsConstructor
public class NightStudyController {

    private final NightStudyUseCase useCase;

    @PostMapping
    public Response apply(@RequestBody @Valid ApplyNightStudyReq req) {
        return useCase.apply(req);
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable Long id) {
        return useCase.cancel(id);
    }

    @PatchMapping("/{id}/allow")
    public Response allow(@PathVariable Long id) {
        return useCase.allow(id);
    }

    @PatchMapping("/{id}/reject")
    public Response reject(@PathVariable Long id, @RequestBody Optional<RejectNightStudyReq> req) {
        return useCase.reject(id, req);
    }

    @PatchMapping("/{id}/revert")
    public Response revert(@PathVariable Long id) {
        return useCase.revert(id);
    }

    @GetMapping
    public ResponseData<List<NightStudyRes>> getValid() {
        return useCase.getValid();
    }

    @GetMapping("/my")
    public ResponseData<List<NightStudyRes>> getMy() {
        return useCase.getMy();
    }

    @GetMapping("/pending")
    public ResponseData<List<NightStudyRes>> getPending() {
        return useCase.getPending();
    }

}
