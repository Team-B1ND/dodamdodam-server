package b1nd.dodam.restapi.member.presentation;

import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.AuthType;
import b1nd.dodam.restapi.member.application.MemberCommandUseCase;
import b1nd.dodam.restapi.member.application.MemberQueryUseCase;
import b1nd.dodam.restapi.member.application.data.req.*;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
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
    public Response join(HttpServletRequest httpServletReq,
                         @RequestBody @Valid JoinStudentReq req) {
        return commandUseCase.join(httpServletReq, req);
    }

    @PostMapping("/join-teacher")
    public Response join(HttpServletRequest httpServletReq,
                         @RequestBody @Valid JoinTeacherReq req) {
        return commandUseCase.join(httpServletReq, req);
    }

    @PostMapping("/join-parent")
    public Response join(HttpServletRequest httpServletReq,
                         @RequestBody @Valid JoinParentReq req) {
        return commandUseCase.join(httpServletReq, req);
    }

    @PostMapping("/broadcast-club-member")
    public Response apply(@RequestBody @Valid ApplyBroadcastClubMemberReq req) {
        return commandUseCase.apply(req);
    }

    @PostMapping("/auth-code/{type}")
    public Response sendAuthCode(@PathVariable AuthType type,
                                 @RequestBody @Valid AuthCodeReq authCodeReq){
        return commandUseCase.sendAuthCode(type, authCodeReq);
    }

    @PostMapping("/auth-code/{type}/verify")
    public Response verifyAuthCode(HttpServletRequest request,
                                   @PathVariable AuthType type,
                                   @RequestBody @Valid VerifyAuthCodeReq req){
        return commandUseCase.verifyAuthCode(request, type, req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable String id) {
        return commandUseCase.delete(id);
    }

    @PatchMapping("/status/{id}")
    public Response updateStatus(@PathVariable String id, @RequestParam ActiveStatus status){
        return commandUseCase.status(id, status);
    }

    @PatchMapping("/deactivate")
    public Response deactivate() {
        return commandUseCase.deactivate();
    }

    @PatchMapping("/deactivate-graduate")
    public Response deactivateGraduate(){
        return commandUseCase.deactivateThirdGrade();
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

    @PatchMapping("/teacher/info/{id}")
    public Response updateTeacherForAdmin(@PathVariable String id, @RequestBody UpdateTeacherForAdminReq req){
        return commandUseCase.updateTeacherForAdmin(id, req);
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

    @GetMapping("/status")
    public ResponseData<List<MemberInfoRes>> getMembersByStatus(@RequestParam ActiveStatus status) {
        return queryUseCase.getMembersByStatus(status);
    }

    @GetMapping("/all")
    public ResponseData<List<MemberInfoRes>> getMemberAll(){
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
