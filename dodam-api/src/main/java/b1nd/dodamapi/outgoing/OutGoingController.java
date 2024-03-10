package b1nd.dodamapi.outgoing;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.outgoing.application.dto.res.OutGoingRes;
import b1nd.dodamcore.outgoing.domain.enums.OutGoingStatus;
import b1nd.dodamcore.outgoing.application.OutGoingService;
import b1nd.dodamcore.outgoing.application.dto.req.ApplyOutGoingReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/out-going")
@RequiredArgsConstructor
public class OutGoingController {

    private final OutGoingService outGoingService;

    @PostMapping
    public Response apply(@RequestBody @Valid ApplyOutGoingReq req) {
        outGoingService.apply(req);

        return Response.created("외출 신청 성공");
    }

    @PatchMapping("/{id}/allow")
    public Response allow(@PathVariable Long id) {
        outGoingService.modifyStatus(id, OutGoingStatus.ALLOWED);

        return Response.noContent("외출 승인 성공");
    }

    @PatchMapping("/{id}/reject")
    public Response reject(@PathVariable Long id) {
        outGoingService.modifyStatus(id, OutGoingStatus.REJECTED);

        return Response.noContent("외출 거절 성공");
    }

    @PatchMapping("/{id}/revert")
    public Response revert(@PathVariable Long id) {
        outGoingService.modifyStatus(id, OutGoingStatus.PENDING);

        return Response.noContent("외출 대기 성공");
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable Long id) {
        outGoingService.cancel(id);

        return Response.noContent("외출 취소 완료");
    }

    @GetMapping
    public ResponseData<List<OutGoingRes>> getByDate(@RequestParam LocalDate date) {
        return ResponseData.ok("날짜별 외출 조회 성공", outGoingService.getByDate(date));
    }

    @GetMapping("/my")
    public ResponseData<List<OutGoingRes>> getMy() {
        return ResponseData.ok("내 외출 조회 성공", outGoingService.getMy());
    }

}