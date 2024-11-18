package b1nd.dodam.restapi.group.presentation;

import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.group.application.GroupUseCase;
import b1nd.dodam.restapi.group.application.data.req.ManageGroupReq;
import b1nd.dodam.restapi.group.application.data.res.GroupMemberRes;
import b1nd.dodam.restapi.group.application.data.res.MyGroupRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupUseCase useCase;

    @PostMapping
    public Response organize(@RequestBody ManageGroupReq req){
        return useCase.organize(req);
    }

    @PostMapping("/member/join/{id}")
    public Response apply(@PathVariable Long id){
        return useCase.applyGroup(id);
    }

    @PatchMapping("apply/{id}")
    public Response handleGroupApplication(@PathVariable Long id, @RequestParam ApprovalStatus status){
        return useCase.handleGroupApplication(id, status);
    }

    @PatchMapping("/{id}")
    public Response modify(
            @PathVariable Long id,
            @RequestBody ManageGroupReq req
    ){
        return useCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id){
        return useCase.delete(id);
    }

    @DeleteMapping("/member/{id}")
    public Response deleteMember(@PathVariable Long id){
        return useCase.exitFromGroupById(id);
    }

    @GetMapping("/my/{status}")
    public ResponseData<List<MyGroupRes>> getMyGroups(@PathVariable ApprovalStatus status) {
        return useCase.getMyGroups(status);
    }

    @GetMapping("/members/{id}")
    public ResponseData<List<GroupMemberRes>> getMyGroup(@PathVariable Long id){
        return useCase.getGroupMemberByGroupId(id);
    }
}