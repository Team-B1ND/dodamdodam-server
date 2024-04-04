package b1nd.dodamapi.member.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.member.usecase.req.*;
import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.event.StudentRegisteredEvent;
import b1nd.dodamcore.member.domain.exception.ActiveMemberException;
import b1nd.dodamcore.member.domain.exception.BroadcastClubMemberDuplicateException;
import b1nd.dodamcore.member.domain.exception.MemberDuplicateException;
import b1nd.dodamcore.member.domain.exception.MemberNotFoundException;
import b1nd.dodamcore.member.domain.exception.StudentNotFoundException;
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
        throwExceptionWhenIdIsDuplicate(req.id());

        Member member = service.save(req.mapToMember(encodePw(req.pw())));
        Student student = service.save(req.mapToStudent(member));
        publishStudentRegisteredEvent(student);
        return Response.created("학생 회원가입 성공");
    }

    private void publishStudentRegisteredEvent(Student student) {
        eventPublisher.publishEvent(new StudentRegisteredEvent(student));
    }

    public Response join(JoinTeacherReq req) {
        throwExceptionWhenIdIsDuplicate(req.id());

        Member member = service.save(req.mapToMember(encodePw(req.pw())));
        service.save(req.mapToTeacher(member));
        return Response.created("선생님 회원가입 성공");
    }

    private void throwExceptionWhenIdIsDuplicate(String id) {
        if(service.checkIdDuplication(id)) {
            throw new MemberDuplicateException();
        }
    }

    private String encodePw(String rawPw) {
        return passwordEncoder.encode(rawPw);
    }

    public Response apply(ApplyBroadcastClubMemberReq req) {
        Member member = getMemberById(req.id());
        throwExceptionWhenMemberIsBroadcastClubMember(member);

        service.save(req.toEntity(member));
        return Response.created("방송부원 등록 성공");
    }

    private void throwExceptionWhenMemberIsBroadcastClubMember(Member member) {
        if(service.checkBroadcastClubMember(member)) {
            throw new BroadcastClubMemberDuplicateException();
        }
    }

    public Response delete(String id) {
        Member member = getMemberById(id);
        throwExceptionWhenAuthStatusIsActive(member);

        service.delete(member);
        return Response.noContent("멤버 삭제 성공");
    }

    private void throwExceptionWhenAuthStatusIsActive(Member member) {
        if(member.isActive()) {
            throw new ActiveMemberException();
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
        Member member = getMemberById(id);
        member.updateStatus(status);
    }

    private Member getMemberById(String id) {
        return service.getMemberById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Response updatePassword(UpdatePasswordReq req) {
        Member member = sessionHolder.current();
        member.updatePw(req.password(), passwordEncoder);

        service.save(member);
        return Response.noContent("비밀번호 수정 성공");
    }

    public Response updateMemberInfo(UpdateMemberInfoReq req) {
        Member member = sessionHolder.current();
        member.updateInfo(req.name(), req.email(), req.phone(), req.profileImage());

        service.save(member);
        return Response.noContent("내 정보 수정 성공");
    }

    public Response updateStudentInfo(UpdateStudentInfoReq req) {
        Student student = getStudentByMember(sessionHolder.current());
        student.updateInfo(req.grade(), req.room(), req.number());

        return Response.noContent("내 학생 정보 수정 성공");
    }

    private Student getStudentByMember(Member member) {
        return service.getStudentByMember(member)
                .orElseThrow(StudentNotFoundException::new);
    }

}