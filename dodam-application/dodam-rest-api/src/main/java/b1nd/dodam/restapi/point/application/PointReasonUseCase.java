package b1nd.dodam.restapi.point.application;

import b1nd.dodam.domain.rds.point.entity.PointReason;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.exception.PointReasonDuplicateException;
import b1nd.dodam.domain.rds.point.repository.PointReasonRepository;
import b1nd.dodam.restapi.point.application.data.req.ModifyPointReasonReq;
import b1nd.dodam.restapi.point.application.data.req.RegisterPointReasonReq;
import b1nd.dodam.restapi.point.application.data.res.PointReasonRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PointReasonUseCase {

    private final PointReasonRepository repository;

    public Response register(RegisterPointReasonReq req) {
        checkIfReasonIsDuplicate(req.reason());
        repository.save(req.toEntity());
        return Response.created("상벌점 사유 등록 성공");
    }

    private void checkIfReasonIsDuplicate(String reason) {
        if(repository.existsByReason(reason)) {
            throw new PointReasonDuplicateException();
        }
    }

    //todo 해당 사유로 발급된 상벌점 처리 필요
    public Response delete(Integer id) {
        repository.deleteById(id);
        return Response.noContent("상벌점 사유 삭제 성공");
    }

    public Response update(int id, ModifyPointReasonReq req) {
        PointReason reason = repository.getById(id);
        reason.update(req.score(), req.reason(), req.scoreType(), req.pointType());
        return Response.noContent("상벌점 사유 수정 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<PointReasonRes>> getBy(PointType type) {
        List<PointReasonRes> result = repository.findByPointType(type)
                .parallelStream()
                .map(PointReasonRes::of)
                .toList();
        return ResponseData.ok("상벌점 사유 조회 성공", result);
    }

}
