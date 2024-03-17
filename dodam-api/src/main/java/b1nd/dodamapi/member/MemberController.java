package b1nd.dodamapi.member;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamapi.member.usecase.MemberCommandUseCase;
import b1nd.dodamapi.member.usecase.MemberQueryUseCase;
import b1nd.dodamapi.member.usecase.req.ApplyBroadcastClubMemberReq;
import b1nd.dodamapi.member.usecase.req.JoinStudentReq;
import b1nd.dodamapi.member.usecase.req.JoinTeacherReq;
import b1nd.dodamcore.member.domain.vo.MemberInfoRes;
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
    public Response apply(@RequestBody ApplyBroadcastClubMemberReq req) {
        return commandUseCase.apply(req);
    }

    @PatchMapping("/{id}/active")
    public Response active(@PathVariable("id") String id) {
        return commandUseCase.active(id);
    }

    @PatchMapping("/{id}/deactivate")
    public Response deactivate(@PathVariable("id") String id) {
        return commandUseCase.deactivate(id);
    }

    @GetMapping("/my")
    public ResponseData<MemberInfoRes> getMy() {
        return queryUseCase.getMyInfo();
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