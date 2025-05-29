package b1nd.dodam.restapi.member.presentation;

import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.redis.member.enumeration.AuthType;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.member.application.MemberCommandUseCase;
import b1nd.dodam.restapi.member.application.MemberQueryUseCase;
import b1nd.dodam.restapi.member.application.data.req.*;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;
import b1nd.dodam.restapi.member.application.data.res.StudentRelationRes;
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
        return commandUseCase.join(MemberAuthenticationHolder.getUserAgent(httpServletReq), req);
    }

    @PostMapping("/join-teacher")
    public Response join(HttpServletRequest httpServletReq,
                         @RequestBody @Valid JoinTeacherReq req) {
        return commandUseCase.join(MemberAuthenticationHolder.getUserAgent(httpServletReq), req);
    }

    @PostMapping("/join-parent")
    public Response join(HttpServletRequest httpServletReq,
                         @RequestBody @Valid JoinParentReq req) {
        return commandUseCase.join(MemberAuthenticationHolder.getUserAgent(httpServletReq), req);
    }

    @PostMapping("/broadcast-club-member")
    public Response applyBroadcastClubMember(@RequestBody @Valid ApplyBroadcastClubMemberReq req) {
        return commandUseCase.applyBroadcastClubMember(req);
    }

    @PostMapping("/dormitory-manage-member")
    public Response applyDormitoryManageMember(@RequestBody @Valid ApplyDormitoryManageMemberReq req) {
        return commandUseCase.applyDormitoryManageMember(req);
    }

    @PostMapping("/auth-code/{type}")
    public Response sendAuthCode(@PathVariable AuthType type,
                                 @RequestBody @Valid AuthCodeReq authCodeReq){
        return commandUseCase.sendAuthCode(type, authCodeReq);
    }

    @PostMapping("/auth-code/{type}/verify")
    public Response verifyAuthCode(HttpServletRequest httpServletReq,
                                   @PathVariable AuthType type,
                                   @RequestBody @Valid VerifyAuthCodeReq req){
        return commandUseCase.verifyAuthCode(MemberAuthenticationHolder.getUserAgent(httpServletReq), type, req);
    }

    @PostMapping("/relation")
    public Response addChild(@RequestBody ConnectStudentReq req){
        return commandUseCase.addChild(req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable String id) {
        return commandUseCase.delete(id);
    }

    @DeleteMapping("/broadcast-club-member")
    public Response removeBroadcastClubMember(@RequestBody @Valid ApplyBroadcastClubMemberReq req){
        return commandUseCase.removeBroadcastClubMember(req);
    }

    @DeleteMapping("/dormitory-manage-member")
    public Response removeDormitoryManageMember(@RequestBody @Valid ApplyDormitoryManageMemberReq req){
        return commandUseCase.removeDormitoryManageMember(req);
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
    public ResponseData<List<MemberInfoRes>> searchMember(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer grade,
            @RequestParam(required = false) MemberRole role,
            @RequestParam(required = false) ActiveStatus status,
            @RequestParam long page,
            @RequestParam long pageSize
    ) {
        return queryUseCase.searchByMemberInfo(name, grade, role, status, page, pageSize);
    }

    @GetMapping("/search/info")
    public ResponseData<List<MemberInfoRes>> searchByInfo(
            @RequestParam String name,
            @RequestParam(required = false) Integer grade,
            @RequestParam MemberRole role,
            @RequestParam ActiveStatus status,
            @RequestParam long page,
            @RequestParam long pageSize
    ) {
        return queryUseCase.searchByMemberInfo(name, grade, role, status, page, pageSize);
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

    @GetMapping("/code/{code}")
    public ResponseData<MemberInfoRes> getMemberByCode(@PathVariable String code){
        return queryUseCase.getMemberByCode(code);
    }

    @GetMapping("/relation")
    public ResponseData<List<StudentRelationRes>> getStudentRelations(){
        return queryUseCase.getStudentByPatent();
    }

}
