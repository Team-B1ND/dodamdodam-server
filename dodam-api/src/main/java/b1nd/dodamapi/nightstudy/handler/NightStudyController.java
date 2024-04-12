package b1nd.dodamapi.nightstudy.handler;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamapi.nightstudy.usecase.NightStudyUseCase;
import b1nd.dodamcore.nightstudy.application.NightStudyService;
import b1nd.dodamcore.nightstudy.application.dto.req.ApplyNightStudyReq;
import b1nd.dodamcore.nightstudy.application.dto.req.RejectNightStudyReq;
import b1nd.dodamcore.nightstudy.application.dto.res.NightStudyRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/night-study")
@RequiredArgsConstructor
public class NightStudyController {

    private final NightStudyUseCase useCase;
    private final NightStudyService nightStudyService;

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
    public Response reject(@PathVariable Long id, @RequestBody RejectNightStudyReq req) {
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
