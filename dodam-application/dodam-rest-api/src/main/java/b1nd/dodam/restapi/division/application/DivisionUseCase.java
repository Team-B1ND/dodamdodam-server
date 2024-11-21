package b1nd.dodam.restapi.division.application;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.division.event.JoinMemberToDivisionEvent;
import b1nd.dodam.domain.rds.division.service.DivisionMemberService;
import b1nd.dodam.domain.rds.division.service.DivisionService;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.division.application.data.req.ManageDivisionReq;
import b1nd.dodam.restapi.division.application.data.res.DivisionDetailRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionMemberCountRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionMemberRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionOverviewRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DivisionUseCase {
    private final DivisionService divisionService;
    private final StudentRepository studentRepository;
    private final DivisionMemberService divisionMemberService;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberAuthenticationHolder authenticationHolder;

    public Response organize(ManageDivisionReq req){
        Division division = req.toEntity();
        divisionService.save(division);
        publishOrganizeDivisionEvent(division, DivisionPermission.ADMIN, ApprovalStatus.ALLOWED);
        return Response.created("조직 구성 성공");
    }

    public Response modify(Long id, ManageDivisionReq req){
        Division division = getDivisionByIdWithValidateAdmin(id);
        division.modify(req.name());
        divisionService.save(division);
        return Response.noContent("조직 정보 수정 성공");
    }

    public Response delete(Long id){
        Division division = getDivisionByIdWithValidateAdmin(id);
        divisionMemberService.deleteByDivision(division);
        divisionService.delete(id);
        return Response.noContent("조직 삭제 성공");
    }

    public Response addMember(Long id){
        publishOrganizeDivisionEvent(
                getDivisionByIdWithValidateAdmin(id),
                DivisionPermission.ADMIN,
                ApprovalStatus.ALLOWED
        );
        return Response.created("조직에 멤버 추가 성공");
    }

    public Response exitFromDivisionById(Long id){
        divisionMemberService.deleteById(id);
        return Response.ok("조직 내 멤버 추방 및 탈퇴");
    }

    public Response applyDivision(Long id){
        Division division = divisionService.getById(id);
        publishOrganizeDivisionEvent(division, DivisionPermission.READER, ApprovalStatus.PENDING);
        return Response.ok("조직에 가입 신청 성공");
    }

    public Response handleDivisionApplication(Long id, ApprovalStatus status){
        DivisionMember divisionMember = divisionMemberService.getById(id);
        divisionMember.modifyStatus(status);
        divisionMemberService.save(divisionMember);
        return Response.ok("조직 수락/거절/취소 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<DivisionDetailRes> getDetail(Long id, ApprovalStatus status){
        Division division = divisionService.getById(id);
        DivisionMember divisionMember = divisionMemberService
                .getByDivisionAndMemberAndStatus(division, authenticationHolder.current(), status);
        return ResponseData.ok("id로 조직 조회 성공", DivisionDetailRes.of(division, divisionMember));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<DivisionOverviewRes>> getMyDivisions(Long lastId, int size){
        List<Division> divisions = divisionService.getByMember(
                authenticationHolder.current(),
                lastId,
                size
        );
        return ResponseData.ok("내 조직 조회 성공", DivisionOverviewRes.of(divisions));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<DivisionOverviewRes>> getDivisions(Long lastId, int size){
        return ResponseData.ok(
                "전체 조직 조회 성공",
                DivisionOverviewRes.of(divisionService.getAll(lastId, size))
        );
    }

    @Transactional(readOnly = true)
    public ResponseData<List<DivisionMemberRes>> getDivisionMemberByDivisionId(Long id){
        Division division = divisionService.getById(id);
        List<DivisionMemberRes> response = divisionMemberService.getByDivision(division)
                .parallelStream()
                .map(dm -> DivisionMemberRes.of(dm, studentRepository.getByMember(dm.getMember())))
                .toList();
        return ResponseData.ok("조직 내 멤버 조회 성공", response);
    }

    @Transactional(readOnly = true)
    public ResponseData<DivisionMemberCountRes> getDivisionMemberCount(Long id, ApprovalStatus status){
        Division division = divisionService.getById(id);
        return ResponseData.ok(
                "조직의 상태별 멤버 수 조회 성공",
                new DivisionMemberCountRes(divisionMemberService.getCountByDivision(division, status))
        );
    }

    private Division getDivisionByIdWithValidateAdmin(Long id){
        Division division = divisionService.getById(id);
        divisionMemberService.validateIsAdminInDivision(division, authenticationHolder.current());
        return division;
    }

    private void publishOrganizeDivisionEvent(Division division, DivisionPermission permission, ApprovalStatus status){
        eventPublisher.publishEvent(
                new JoinMemberToDivisionEvent(permission, authenticationHolder.current(), division, status));
    }
}
