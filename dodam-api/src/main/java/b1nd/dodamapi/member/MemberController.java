package b1nd.dodamapi.member;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.dto.req.StudentJoinReq;
import b1nd.dodamcore.member.application.dto.req.TeacherJoinReq;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join-student")
    public Response joinStudent(@RequestBody @Valid StudentJoinReq studentJoinReq) {
        memberService.joinStudent(studentJoinReq);
        return Response.ok("학생 회원가입 성공");
    }

    @PostMapping("/join-teacher")
    public Response joinTeacher(@RequestBody @Valid TeacherJoinReq teacherJoinReq) {
        memberService.joinTeacher(teacherJoinReq);
        return Response.ok("선생님 회원가입 성공");
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

}
