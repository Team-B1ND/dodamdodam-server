package b1nd.dodam.restapi.recruitment.application;

import b1nd.dodam.domain.rds.recruitment.entity.Recruitment;
import b1nd.dodam.domain.rds.recruitment.service.RecruitmentService;
import b1nd.dodam.domain.rds.recruitment.service.data.RecruitPageRes;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.recruitment.application.data.req.RecruitReq;
import b1nd.dodam.restapi.recruitment.application.data.res.RecruitRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RecruitmentUseCase {

    private final RecruitmentService recruitmentService;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public Response create(RecruitReq req) {
        Recruitment recruitment = req.mapToRecruit(memberAuthenticationHolder.current());
        recruitmentService.save(recruitment);
        return Response.created("채용 의뢰서 작성 성공");
    }

    public Response modify(int recruitmentId, RecruitReq req) {
        Recruitment recruitment = recruitmentService.getById(recruitmentId);
        recruitmentService.deleteFileByRecruitmentId(recruitmentId);
        recruitment.updateRecruit(
                req.name(),
                req.location(),
                req.duty(),
                req.etc(),
                req.personnel(),
                req.image(),
                req.updateRecruitFile(recruitment)
        );
        return Response.noContent("채용 의뢰서 수정 성공");
    }

    public Response delete(int recruitmentId) {
        recruitmentService.deleteById(recruitmentId);
        return Response.noContent("채용 의뢰서 삭제 성공");
    }

    public ResponseData<RecruitRes> getById(int id) {
        return ResponseData.ok("채용 의뢰서 단일 조회 성공", RecruitRes.of(recruitmentService.getById(id)));
    }

    public ResponseData<RecruitPageRes> getAllOrderByIdDesc(int page, int size) {
        return ResponseData.ok("채용 의뢰서 조회 성공", recruitmentService.getAllOrderByIdDesc(page, size));
    }

}
