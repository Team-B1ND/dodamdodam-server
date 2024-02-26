package b1nd.dodamapi.point;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.point.application.PointReasonService;
import b1nd.dodamcore.point.application.PointService;
import b1nd.dodamcore.point.application.dto.req.IssuePointReq;
import b1nd.dodamcore.point.application.dto.req.ModifyPointReasonReq;
import b1nd.dodamcore.point.application.dto.req.RegisterPointReasonReq;
import b1nd.dodamcore.point.application.dto.res.PointReasonRes;
import b1nd.dodamcore.point.application.dto.res.PointRes;
import b1nd.dodamcore.point.application.dto.res.PointScoreRes;
import b1nd.dodamcore.point.domain.enums.PointType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final PointReasonService pointReasonService;

    @PostMapping
    public Response issue(@RequestBody @Valid IssuePointReq req) {
        pointService.issue(req);

        return Response.created("상벌점 발급 성공");
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable int id) {
        pointService.cancel(id);

        return Response.noContent("상벌점 취소 성공");
    }

    @GetMapping("/my")
    public ResponseData<List<PointRes>> getMyPointsBy(@RequestParam PointType type) {
        return ResponseData.ok("내 상벌점 조회 성공", pointService.getMyByPointType(type));
    }

    @GetMapping("/student/{studentId}")
    public ResponseData<List<PointRes>> getPointsBy(@PathVariable int studentId, @RequestParam PointType type) {
        return ResponseData.ok("학생별 상벌점 조회 성공", pointService.getByStudentAndPointType(studentId, type));
    }

    @GetMapping("/score/my")
    public ResponseData<PointScoreRes> getMyScoreBy(@RequestParam PointType type) {
        return ResponseData.ok("내 상벌점 점수 조회 성공", pointService.getMyScoreByPointType(type));
    }

    @GetMapping("/score/all")
    public ResponseData<List<PointScoreRes>> getAll(@RequestParam PointType type) {
        return ResponseData.ok("모든 상벌점 점수 조회 성공", pointService.getAllScoreByPointType(type));
    }

    @PostMapping("/reason")
    public Response register(@RequestBody @Valid RegisterPointReasonReq req) {
        pointReasonService.register(req);

        return Response.created("상벌점 사유 등록 성공");
    }

    @DeleteMapping("/reason/{id}")
    public Response delete(@PathVariable int id) {
        pointReasonService.delete(id);

        return Response.noContent("상벌점 사유 삭제 성공");
    }

    @PatchMapping("/reason/{id}")
    public Response modify(@PathVariable int id, @RequestBody ModifyPointReasonReq req) {
        pointReasonService.modify(id, req);

        return Response.noContent("상벌점 사유 수정 성공");
    }

    @GetMapping("/reason")
    public ResponseData<List<PointReasonRes>> getBy(@RequestParam PointType type) {
        return ResponseData.ok("상벌점 사유 조회 성공", pointReasonService.getByPointType(type));
    }

}