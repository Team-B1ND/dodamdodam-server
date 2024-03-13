package b1nd.dodamapi.member;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.dto.req.ApplyBroadcastClubMemberReq;
import b1nd.dodamcore.member.application.dto.req.JoinStudentReq;
import b1nd.dodamcore.member.application.dto.req.JoinTeacherReq;
import b1nd.dodamcore.member.application.dto.res.MemberInfoRes;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join-student")
    public Response joinStudent(@RequestBody @Valid JoinStudentReq req) {
        memberService.joinStudent(req);
        return Response.created("학생 회원가입 성공");
    }

    @PostMapping("/join-teacher")
    public Response joinTeacher(@RequestBody @Valid JoinTeacherReq req) {
        memberService.joinTeacher(req);
        return Response.created("선생님 회원가입 성공");
    }

    @PostMapping("/broadcast-club-member")
    public Response applyBroadCastClubMember(@RequestBody ApplyBroadcastClubMemberReq req) {
        memberService.applyBroadcastClubMember(req);
        return Response.created("방송부원 등록 성공");
    }

    @PatchMapping("/{id}/active")
    public Response active(@PathVariable("id") String id) {
        memberService.modifyStatus(id, AuthStatus.ACTIVE);
        return Response.ok("멤버 활성화 성공");
    }

    @PatchMapping("/{id}/deactivate")
    public Response deactivate(@PathVariable("id") String id) {
        memberService.modifyStatus(id, AuthStatus.DEACTIVATE);
        return Response.ok("멤버 비활성화 성공");
    }

    @GetMapping("/my")
    public ResponseData<MemberInfoRes> getMyInfo() {
        return ResponseData.ok("내 정보 조회 성공", memberService.getMyInfo());
    }

    @GetMapping("/all")
    public ResponseData<List<MemberInfoRes>> getAll() {
        return ResponseData.ok("모든 멤버 정보 조회 성공", memberService.getAllInfo());
    }

    @GetMapping("/check/broadcast-club-member")
    public ResponseData<Boolean> checkBroadcastClubMember() {
        return ResponseData.ok("방송부원 확인 성공", memberService.checkBroadcastClubMember());
    }

}