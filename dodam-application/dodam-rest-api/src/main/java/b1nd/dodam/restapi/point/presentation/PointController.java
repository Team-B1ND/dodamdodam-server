package b1nd.dodam.restapi.point.presentation;

import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.restapi.point.application.PointReasonUseCase;
import b1nd.dodam.restapi.point.application.PointUseCase;
import b1nd.dodam.restapi.point.application.data.req.IssuePointReq;
import b1nd.dodam.restapi.point.application.data.req.ModifyPointReasonReq;
import b1nd.dodam.restapi.point.application.data.req.RegisterPointReasonReq;
import b1nd.dodam.restapi.point.application.data.res.PointReasonRes;
import b1nd.dodam.restapi.point.application.data.res.PointRes;
import b1nd.dodam.restapi.point.application.data.res.PointScoreRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointUseCase pointUseCase;
    private final PointReasonUseCase pointReasonUseCase;

    @PostMapping
    public Response issue(@RequestBody @Valid IssuePointReq req) {
        return pointUseCase.issue(req);
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable Integer id) {
        return pointUseCase.cancel(id);
    }

    @GetMapping("/my")
    public ResponseData<List<PointRes>> getMyPointsBy(@RequestParam PointType type) {
        return pointUseCase.getMyPoints(type);
    }

    @GetMapping("/student/{id}")
    public ResponseData<List<PointRes>> getPointsBy(@PathVariable("id") Integer studentId, @RequestParam PointType type) {
        return pointUseCase.getPointsByStudent(studentId, type);
    }

    @GetMapping("/score/my")
    public ResponseData<PointScoreRes> getMyScoreBy(@RequestParam PointType type) {
        return pointUseCase.getMyScore(type);
    }

    @GetMapping("/score/all")
    public ResponseData<List<PointScoreRes>> getAll(@RequestParam PointType type) {
        return pointUseCase.getAllScores(type);
    }

    @PostMapping("/reason")
    public Response register(@RequestBody @Valid RegisterPointReasonReq req) {
        return pointReasonUseCase.register(req);
    }

    @DeleteMapping("/reason/{id}")
    public Response delete(@PathVariable Integer id) {
        return pointReasonUseCase.delete(id);
    }

    @PatchMapping("/reason/{id}")
    public Response update(@PathVariable Integer id, @RequestBody ModifyPointReasonReq req) {
        return pointReasonUseCase.update(id, req);
    }

    @GetMapping("/reason")
    public ResponseData<List<PointReasonRes>> getBy(@RequestParam PointType type) {
        return pointReasonUseCase.getBy(type);
    }

}
