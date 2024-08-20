package b1nd.dodam.restapi.member.presentation;

import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.restapi.member.application.MemberCommandUseCase;
import b1nd.dodam.restapi.member.application.MemberQueryUseCase;
import b1nd.dodam.restapi.member.application.data.req.*;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandUseCase commandUseCase;
    private final MemberQueryUseCase queryUseCase;

    @PostMapping("/join-student")
    public Response join(@RequestBody @Valid JoinStudentReq req) {
        return commandUseCase.join(req);
    }

    @PostMapping("/join-teacher")
    public Response join(@RequestBody @Valid JoinTeacherReq req) {
        return commandUseCase.join(req);
    }

    @PostMapping("/broadcast-club-member")
    public Response apply(@RequestBody @Valid ApplyBroadcastClubMemberReq req) {
        return commandUseCase.apply(req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable String id) {
        return commandUseCase.delete(id);
    }

    @PatchMapping("/active/{id}")
    public Response active(@PathVariable("id") String id) {
        return commandUseCase.active(id);
    }

    @PatchMapping("/deactivate")
    public Response deactivate() {
        return commandUseCase.deactivate();
    }

    @PatchMapping("/deactivate/{id}")
    public Response deactivate(@PathVariable("id") String id) {
        return commandUseCase.deactivate(id);
    }

    @PatchMapping("/password")
    public Response updatePassword(@RequestBody @Valid UpdatePasswordReq req) {
        return commandUseCase.updatePassword(req);
    }

    @PatchMapping("/info")
    public Response updateMemberInfo(@RequestBody UpdateMemberInfoReq req) {
        return commandUseCase.updateMemberInfo(req);
    }

    @PatchMapping("/student/info")
    public Response updateStudentInfo(@RequestBody UpdateStudentInfoReq req) {
        return commandUseCase.updateStudentInfo(req);
    }

    @PatchMapping("/info/{id}/{role}")
    public Response updateInfoForAdmin(@PathVariable String id,
                                       @PathVariable MemberRole role,
                                       @RequestBody UpdateStudentForAdminReq req
    ){
        return commandUseCase.updateInfoForAdmin(id, role, req);
    }

    @GetMapping("/{id}")
    public ResponseData<MemberInfoRes> getById(@PathVariable String id) {
        return queryUseCase.getById(id);
    }

    @GetMapping("/my")
    public ResponseData<MemberInfoRes> getMy() {
        return queryUseCase.getMyInfo();
    }

    @GetMapping("/search")
    public ResponseData<List<MemberInfoRes>> searchByName(@RequestParam String name) {
        return queryUseCase.searchByName(name);
    }

    @GetMapping("/deactivate")
    public ResponseData<List<MemberInfoRes>> getDeactivateMembers() {
        return queryUseCase.getDeactivateMembers();
    }

    @GetMapping("/all")
    public ResponseData<List<MemberInfoRes>> getAll() {
        return queryUseCase.getAll();
    }

    @GetMapping("/check/broadcast-club-member")
    public ResponseData<Boolean> checkBroadcastClubMember() {
        return queryUseCase.checkBroadcastClubMember();
    }

    @GetMapping("/check/broadcast-club-member/{id}")
    public ResponseData<Boolean> checkBroadcastClubMember(@PathVariable String id) {
        return queryUseCase.checkBroadcastClubMember(id);
    }


}
