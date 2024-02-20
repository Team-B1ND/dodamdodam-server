package b1nd.dodamapi.nightstudy;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.nightstudy.application.NightStudyService;
import b1nd.dodamcore.nightstudy.application.dto.req.ApplyNightStudyReq;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import b1nd.dodamcore.nightstudy.application.dto.res.NightStudyRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/night-study")
@RequiredArgsConstructor
public class NightStudyController {

    private final NightStudyService nightStudyService;

    @PostMapping
    public Response apply(@RequestBody @Valid ApplyNightStudyReq req) {
        nightStudyService.apply(req);

        return Response.ok("심야자습 신청 성공");
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable Long id) {
        nightStudyService.cancel(id);

        return Response.ok("심야자습 취소 성공");
    }

    @PatchMapping("/{id}/allow")
    public Response allow(@PathVariable Long id) {
        nightStudyService.modifyStatus(id, NightStudyStatus.ALLOWED);

        return Response.ok("심야자습 승인 성공");
    }

    @PatchMapping("/{id}/reject")
    public Response reject(@PathVariable Long id) {
        nightStudyService.modifyStatus(id, NightStudyStatus.REJECTED);

        return Response.ok("심야자습 거절 성공");
    }

    @GetMapping
    public ResponseData<List<NightStudyRes>> getValid() {
        return ResponseData.ok("유요한 심야자습 조회 성공", nightStudyService.getValid());
    }

    @GetMapping("/my")
    public ResponseData<List<NightStudyRes>> getMy() {
        return ResponseData.ok("내 심야자습 조회 성공", nightStudyService.getMy());
    }

    @GetMapping("/pending")
    public ResponseData<List<NightStudyRes>> getPending() {
        return ResponseData.ok("대기중인 심야자습 조회 성공", nightStudyService.getPending());
    }

}