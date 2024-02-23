package b1nd.dodamcore.point.application;

import b1nd.dodamcore.point.application.dto.req.ModifyPointReasonReq;
import b1nd.dodamcore.point.application.dto.req.RegisterPointReasonReq;
import b1nd.dodamcore.point.application.dto.res.PointReasonRes;
import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.exception.PointReasonDuplicateException;
import b1nd.dodamcore.point.domain.exception.PointReasonNotFoundException;
import b1nd.dodamcore.point.repository.PointReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointReasonService {

    private final PointReasonRepository reasonRepository;

    @Transactional
    public void register(RegisterPointReasonReq req) {
        if(reasonRepository.existsByReason(req.reason())) {
            throw new PointReasonDuplicateException();
        }

        reasonRepository.save(req.toEntity());
    }

    @Transactional
    public void delete(int id) {
        //todo 해당 사유로 발급된 상벌점 처리 필요
        reasonRepository.deleteById(id);
    }

    @Transactional
    public void modify(int id, ModifyPointReasonReq req) {
        PointReason reason = reasonRepository.findById(id)
                .orElseThrow(PointReasonNotFoundException::new);

        reason.modify(req.score(), req.reason(), req.scoreType(), req.pointType());
    }

    @Transactional(readOnly = true)
    public List<PointReasonRes> getByPointType(PointType type) {
        return reasonRepository.findByPointType(type).stream()
                .map(PointReasonRes::of)
                .toList();
    }

}