package b1nd.dodam.restapi.division.application;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.division.service.DivisionMemberService;
import b1nd.dodam.domain.rds.division.service.DivisionService;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.division.application.data.req.ManageDivisionReq;
import b1nd.dodam.restapi.division.application.data.res.DivisionDetailRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionOverviewRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DivisionUseCase {
    private final DivisionService divisionService;
    private final DivisionMemberService divisionMemberService;
    private final MemberAuthenticationHolder authHolder;

    public Response organize(ManageDivisionReq req){
        divisionService.checkIsNotDuplicateName(req.name());
        Division division = req.toEntity();
        divisionService.save(division);
        divisionMemberService.saveWithBuild(
                division,
                authHolder.current(),
                ApprovalStatus.ALLOWED,
                DivisionPermission.ADMIN
        );
        return Response.created("조직 구성 성공");
    }

    public Response modify(Long id, ManageDivisionReq req){
        Division division = getDivisionByIdWithValidateAdmin(id);
        division.modify(req.name());
        divisionService.save(division);
        return Response.noContent("조직 정보 수정 성공");
    }

    public Response delete(Long id){
        divisionMemberService.deleteByDivision(getDivisionByIdWithValidateAdmin(id));
        divisionService.delete(id);
        return Response.noContent("조직 삭제 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<DivisionDetailRes> getDetail(Long id){
        Division division = divisionService.getById(id);
        DivisionMember divisionMember = divisionMemberService
                .getByDivisionAndMemberAndStatus(division, authHolder.current(), ApprovalStatus.ALLOWED);
        return ResponseData.ok("id로 조직 조회 성공", DivisionDetailRes.of(division, divisionMember));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<DivisionOverviewRes>> getMyDivisions(Long lastId, int size){
        List<Division> divisions = divisionService.getByMember(authHolder.current(), lastId, size);
        return ResponseData.ok("내 조직 조회 성공", DivisionOverviewRes.of(divisions));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<DivisionOverviewRes>> getDivisions(Long lastId, int size){
        return ResponseData.ok("전체 조직 조회 성공", DivisionOverviewRes.of(divisionService.getAll(lastId, size)));
    }

    private Division getDivisionByIdWithValidateAdmin(Long id){
        Division division = divisionService.getById(id);
        divisionMemberService.validateIsAdminInDivision(division, authHolder.current());
        return division;
    }
}
