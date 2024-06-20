package b1nd.dodamapi.point.handler;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamapi.point.usecase.PointReasonUseCase;
import b1nd.dodamapi.point.usecase.PointUseCase;
import b1nd.dodamapi.point.usecase.dto.req.IssuePointReq;
import b1nd.dodamapi.point.usecase.dto.req.ModifyPointReasonReq;
import b1nd.dodamapi.point.usecase.dto.req.RegisterPointReasonReq;
import b1nd.dodamapi.point.usecase.dto.res.PointReasonRes;
import b1nd.dodamapi.point.usecase.dto.res.PointRes;
import b1nd.dodamapi.point.usecase.dto.res.PointScoreRes;
import b1nd.dodamcore.point.domain.enums.PointType;
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
