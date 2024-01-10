package b1nd.dodamapi.member;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.dto.request.StudentJoinRequest;
import b1nd.dodamcore.member.application.dto.request.TeacherJoinRequest;
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
    public Response joinStudent(@RequestBody @Valid StudentJoinRequest studentJoinRequest) {
        memberService.joinStudent(studentJoinRequest);
        return Response.ok("학생 회원가입 성공");
    }

    @PostMapping("/join-teacher")
    public Response joinTeacher(@RequestBody @Valid TeacherJoinRequest teacherJoinRequest) {
        memberService.joinTeacher(teacherJoinRequest);
        return Response.ok("선생님 회원가입 성공");
    }
}
