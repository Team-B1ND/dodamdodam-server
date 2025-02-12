package b1nd.dodam.restapi.division.presentation;

import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.division.application.DivisionMemberUseCase;
import b1nd.dodam.restapi.division.application.DivisionUseCase;
import b1nd.dodam.restapi.division.application.data.req.ManageDivisionReq;
import b1nd.dodam.restapi.division.application.data.res.DivisionDetailRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionMemberCountRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionMemberRes;
import b1nd.dodam.restapi.division.application.data.res.DivisionOverviewRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/divisions")
@RequiredArgsConstructor
public class DivisionController {

    private final DivisionUseCase divisionUseCase;
    private final DivisionMemberUseCase divisionMemberUseCase;

    @PostMapping
    public Response createDivision(@RequestBody ManageDivisionReq req) {
        return divisionUseCase.organize(req);
    }

    @PatchMapping("/{id}")
    public Response updateDivision(
            @PathVariable Long id,
            @RequestBody ManageDivisionReq req
    ) {
        return divisionUseCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response deleteDivision(@PathVariable Long id) {
        return divisionUseCase.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseData<DivisionDetailRes> getDivisionDetail(@PathVariable Long id) {
        return divisionUseCase.getDetail(id);
    }

    @GetMapping
    public ResponseData<List<DivisionOverviewRes>> getAllDivisions(
            @RequestParam Long lastId,
            @RequestParam int limit
    ) {
        return divisionUseCase.getDivisions(lastId, limit);
    }

    @GetMapping("/my")
    public ResponseData<List<DivisionOverviewRes>> getMyDivisions(
            @RequestParam Long lastId,
            @RequestParam int limit
    ) {
        return divisionUseCase.getMyDivisions(lastId, limit);
    }

    @PostMapping("/{id}/members/apply")
    public Response applyToDivision(@PathVariable Long id) {
        return divisionMemberUseCase.applyDivision(id);
    }

    @PostMapping("/{id}/members")
    public Response addMemberToDivision(
            @PathVariable Long id,
            @RequestParam List<String> memberIdList
    ) {
        return divisionMemberUseCase.addMember(id, memberIdList);
    }

    @PatchMapping("/{id}/members")
    public Response handleMemberApplication(
            @PathVariable Long id,
            @RequestParam List<Long> idList,
            @RequestParam ApprovalStatus status
    ) {
        return divisionMemberUseCase.handleDivisionApplication(idList, status);
    }

    @PatchMapping("/{id}/members/{divisionMemberId}/permission")
    public Response handleMemberPermission(
            @PathVariable Long id,
            @PathVariable Long divisionMemberId,
            @RequestParam DivisionPermission permission
    ) {
        return divisionMemberUseCase.handleMemberPermission(divisionMemberId, permission);
    }

    @DeleteMapping("/{id}/members")
    public Response removeMemberFromDivision(
            @PathVariable Long id,
            @RequestParam List<Long> idList
    ) {
        return divisionMemberUseCase.expelMemberFromDivision(idList);
    }

    @GetMapping("/{id}/members")
    public ResponseData<List<DivisionMemberRes>> getMembersByDivision(
            @PathVariable Long id,
            @RequestParam ApprovalStatus status
    ) {
        return divisionMemberUseCase.getDivisionMemberByDivisionId(id, status);
    }

    @GetMapping("/{id}/members/count")
    public ResponseData<DivisionMemberCountRes> getMemberCountByStatus(
            @PathVariable Long id,
            @RequestParam ApprovalStatus status
    ) {
        return divisionMemberUseCase.getDivisionMemberCount(id, status);
    }
}
