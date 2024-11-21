package b1nd.dodam.restapi.division.application;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.division.service.DivisionMemberService;
import b1nd.dodam.domain.rds.division.service.DivisionService;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.division.application.data.res.DivisionMemberCountRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionMemberRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DivisionMemberUseCase {
    private final DivisionMemberService divisionMemberService;
    private final DivisionService divisionService;
    private final MemberRepository memberRepository;
    private final MemberAuthenticationHolder authHolder;
    private final StudentRepository studentRepository;

    public Response addMember(Long id, List<String> memberIdList){
        divisionMemberService.saveAllWithBuild(
                divisionService.getById(id),
                memberIdList.stream().map(memberRepository::getById).toList(),
                ApprovalStatus.ALLOWED,
                DivisionPermission.READER
        );
        return Response.created("조직에 멤버 추가 성공");
    }

    public Response expelMemberFromDivision(List<Long> idList){
        divisionMemberService.deleteByIds(idList);
        return Response.ok("조직 내 멤버 추방");
    }

    public Response applyDivision(Long id){
        divisionMemberService.saveWithBuild(
                divisionService.getById(id),
                authHolder.current(),
                ApprovalStatus.PENDING,
                DivisionPermission.READER
        );
        return Response.ok("조직에 가입 신청 성공");
    }

    public Response handleDivisionApplication(List<Long> idList, ApprovalStatus status) {
        divisionMemberService.modifyDivisionMembers(divisionMemberService.getByIds(idList), status);
        return Response.ok("조직 수락/거절/취소 성공");
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
}
