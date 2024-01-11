package b1nd.dodamapi.member;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.dto.req.StudentJoinReq;
import b1nd.dodamcore.member.application.dto.req.TeacherJoinReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/member")
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
}
