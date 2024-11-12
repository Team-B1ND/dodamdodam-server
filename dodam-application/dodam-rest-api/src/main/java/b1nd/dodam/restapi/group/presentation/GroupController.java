package b1nd.dodam.restapi.group.presentation;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.restapi.group.application.GroupUseCase;
import b1nd.dodam.restapi.group.application.data.req.ManageGroupReq;
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

    @GetMapping("/my")
    public ResponseData<List<MyGroupRes>> getMyGroups() {
        return useCase.getMyGroups();
    }

}