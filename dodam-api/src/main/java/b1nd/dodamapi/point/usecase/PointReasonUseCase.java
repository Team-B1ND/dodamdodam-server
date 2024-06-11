package b1nd.dodamapi.point.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.point.application.PointReasonService;
import b1nd.dodamapi.point.usecase.req.ModifyPointReasonReq;
import b1nd.dodamapi.point.usecase.req.RegisterPointReasonReq;
import b1nd.dodamcore.point.domain.vo.PointReasonRes;
import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.exception.PointReasonDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PointReasonUseCase {

    private final PointReasonService service;

    public Response register(RegisterPointReasonReq req) {
        throwExceptionWhenReasonIsDuplicate(req.reason());
        service.save(req.toEntity());
        return Response.created("상벌점 사유 등록 성공");
    }

    private void throwExceptionWhenReasonIsDuplicate(String reason) {
        if(service.checkReasonDuplication(reason)) {
            throw new PointReasonDuplicateException();
        }
    }

    //todo 해당 사유로 발급된 상벌점 처리 필요
    public Response delete(Integer id) {
        PointReason reason = service.getBy(id);

        service.delete(reason);
        return Response.noContent("상벌점 사유 삭제 성공");
    }

    public Response update(Integer id, ModifyPointReasonReq req) {
        PointReason reason = service.getBy(id);
        reason.update(req.score(), req.reason(), req.scoreType(), req.pointType());

        return Response.noContent("상벌점 사유 수정 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<PointReasonRes>> getBy(PointType type) {
        List<PointReasonRes> result = service.getBy(type).parallelStream()
                .map(PointReasonRes::of)
                .toList();
        return ResponseData.ok("상벌점 사유 조회 성공", result);
    }

}
