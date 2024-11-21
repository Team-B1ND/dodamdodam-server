package b1nd.dodam.restapi.division.presentation;

import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
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
@RequestMapping("/division")
@RequiredArgsConstructor
public class DivisionController {

    private final DivisionUseCase useCase;

    @PostMapping
    public Response organize(@RequestBody ManageDivisionReq req){
        return useCase.organize(req);
    }

    @PostMapping("/member/join/{id}")
    public Response apply(@PathVariable Long id){
        return useCase.applyDivision(id);
    }

    @PostMapping("/member/{id}")
    public Response addMember(@PathVariable Long id){
        return useCase.addMember(id);
    }

    @PatchMapping("/apply/{id}")
    public Response handleDivisionApplication(@PathVariable Long id, @RequestParam ApprovalStatus status){
        return useCase.handleDivisionApplication(id, status);
    }

    @PatchMapping("/{id}")
    public Response modify(
            @PathVariable Long id,
            @RequestBody ManageDivisionReq req
    ){
        return useCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id){
        return useCase.delete(id);
    }

    @DeleteMapping("/member/{id}")
    public Response deleteMember(@PathVariable Long id){
        return useCase.exitFromDivisionById(id);
    }

    @GetMapping("/{id}/{status}")
    public ResponseData<DivisionDetailRes> getDetail(@PathVariable Long id, @PathVariable ApprovalStatus status){
        return useCase.getDetail(id, status);
    }

    @GetMapping("/member/count/{status}")
    public ResponseData<DivisionMemberCountRes> getMemberCount(@PathVariable Long id, @PathVariable ApprovalStatus status){
        return useCase.getDivisionMemberCount(id, status);
    }

    @GetMapping("/my")
    public ResponseData<List<DivisionOverviewRes>> getMyDivisions(
            @RequestParam Long lastId,
            @RequestParam int size
    ) {
        return useCase.getMyDivisions(lastId, size);
    }

    @GetMapping
    public ResponseData<List<DivisionOverviewRes>> getAllDivisions(
            @RequestParam Long lastId,
            @RequestParam int size
    ){
        return useCase.getDivisions(lastId, size);
    }

    @GetMapping("/members/{id}")
    public ResponseData<List<DivisionMemberRes>> getDivisionMemberByGroup(@PathVariable Long id){
        return useCase.getDivisionMemberByDivisionId(id);
    }

}