package b1nd.dodamapi.outsleeping;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.outsleeping.application.OutSleepingService;
import b1nd.dodamcore.outsleeping.application.dto.req.ApplyOutSleepingReq;
import b1nd.dodamcore.outsleeping.application.dto.res.OutSleepingRes;
import b1nd.dodamcore.outsleeping.domain.enums.OutSleepingStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/out-sleeping")
@RequiredArgsConstructor
public class OutSleepingController {

    private final OutSleepingService outSleepingService;

    @PostMapping
    public Response apply(@RequestBody @Valid ApplyOutSleepingReq req) {
        outSleepingService.apply(req);

        return Response.created("외박 신청 성공");
    }

    @PatchMapping("/{id}/allow")
    public Response allow(@PathVariable Long id) {
        outSleepingService.modifyStatus(id, OutSleepingStatus.ALLOWED);

        return Response.noContent("외박 승인 성공");
    }

    @PatchMapping("/{id}/reject")
    public Response reject(@PathVariable Long id) {
        outSleepingService.modifyStatus(id, OutSleepingStatus.REJECTED);

        return Response.noContent("외박 거절 성공");
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable Long id) {
        outSleepingService.cancel(id);

        return Response.noContent("외박 취소 성공");
    }

    @GetMapping
    public ResponseData<List<OutSleepingRes>> getBy(@RequestParam LocalDate date) {
        return ResponseData.ok("날짜별 외박 조회 성공", outSleepingService.getByDate(date));
    }

    @GetMapping("/my")
    public ResponseData<List<OutSleepingRes>> getMy() {
        return ResponseData.ok("내 외박 조회 성공", outSleepingService.getMy());
    }

    @GetMapping("/valid")
    public ResponseData<List<OutSleepingRes>> getValid() {
        return ResponseData.ok("유요한 외박 조회 성공", outSleepingService.getValid());
    }

}