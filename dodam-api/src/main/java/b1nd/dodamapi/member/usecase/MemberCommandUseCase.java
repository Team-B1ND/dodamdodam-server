package b1nd.dodamapi.member.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamapi.member.usecase.req.ApplyBroadcastClubMemberReq;
import b1nd.dodamapi.member.usecase.req.JoinStudentReq;
import b1nd.dodamapi.member.usecase.req.JoinTeacherReq;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.event.StudentRegisteredEvent;
import b1nd.dodamcore.member.domain.exception.BroadcastClubMemberDuplicateException;
import b1nd.dodamcore.member.domain.exception.MemberDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberCommandUseCase {

    private final MemberService service;
    private final MemberSessionHolder sessionHolder;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public Response join(JoinStudentReq req) {
        checkIdDuplication(req.id());
        Member member = req.mapToMember(passwordEncoder.encode(req.pw()));
        Student student = req.mapToStudent(member);

        service.save(member, student);
        publishStudentRegisteredEvent(student);
        return Response.created("학생 회원가입 성공");
    }

    private void publishStudentRegisteredEvent(Student student) {
        eventPublisher.publishEvent(new StudentRegisteredEvent(student));
    }

    public Response join(JoinTeacherReq req) {
        checkIdDuplication(req.id());
        Member member = req.mapToMember(passwordEncoder.encode(req.pw()));
        Teacher teacher = req.mapToTeacher(member);

        service.save(member, teacher);
        return Response.created("선생님 회원가입 성공");
    }

    private void checkIdDuplication(String id) {
        if(service.checkIdDuplication(id)) {
            throw new MemberDuplicateException();
        }
    }

    public Response apply(ApplyBroadcastClubMemberReq req) {
        Member member = sessionHolder.current();
        checkBroadcastClubMemberDuplication(member);

        service.save(req.toEntity(member));
        return Response.created("방송부원 등록 성공");
    }

    private void checkBroadcastClubMemberDuplication(Member member) {
        if(service.checkBroadcastClubMember(member)) {
            throw new BroadcastClubMemberDuplicateException();
        }
    }

    public Response active(String id) {
        updateStatus(id, AuthStatus.ACTIVE);
        return Response.ok("멤버 활성화 성공");
    }

    public Response deactivate(String id) {
        updateStatus(id, AuthStatus.DEACTIVATE);
        return Response.ok("멤버 비활성화 성공");
    }

    private void updateStatus(String id, AuthStatus status) {
        Member member = service.getById(id);
        member.updateStatus(status);
    }

}