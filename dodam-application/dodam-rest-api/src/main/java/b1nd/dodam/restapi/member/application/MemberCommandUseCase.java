package b1nd.dodam.restapi.member.application;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.event.StudentRegisteredEvent;
import b1nd.dodam.domain.rds.member.exception.*;
import b1nd.dodam.domain.rds.member.service.MemberService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.member.application.data.req.*;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.encrypt.Sha512PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberCommandUseCase {

    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final MemberService service;
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
        return Sha512PasswordEncoder.encode(rawPw);
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
        updateStatus(id, ActiveStatus.ACTIVE);
        return Response.ok("멤버 활성화 성공");
    }

    public Response deactivate(String id) {
        updateStatus(id, ActiveStatus.DEACTIVATE);
        return Response.ok("멤버 비활성화 성공");
    }

    public Response deactivate() {
        Member member = memberAuthenticationHolder.current();
        member.updateStatus(ActiveStatus.DEACTIVATE);
        service.save(member);
        return Response.ok("멤버 비활성화 성공");
    }

    private void updateStatus(String id, ActiveStatus status) {
        Member member = getMemberById(id);
        member.updateStatus(status);
    }

    private Member getMemberById(String id) {
        return service.getMemberById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Response updatePassword(UpdatePasswordReq req) {
        Member member = memberAuthenticationHolder.current();
        member.updatePw(Sha512PasswordEncoder.encode(req.password()));
        service.save(member);
        return Response.noContent("비밀번호 수정 성공");
    }

    public Response updateMemberInfo(UpdateMemberInfoReq req) {
        Member member = memberAuthenticationHolder.current();
        member.updateInfo(req.name(), req.email(), req.phone(), req.profileImage());
        service.save(member);
        return Response.noContent("내 정보 수정 성공");
    }

    public Response updateStudentInfo(UpdateStudentInfoReq req) {
        Student student = getStudentByMember(memberAuthenticationHolder.current());
        student.updateInfo(req.grade(), req.room(), req.number());
        return Response.noContent("내 학생 정보 수정 성공");
    }

    public Response updateStudentForAdmin(String id, UpdateStudentForAdminReq req){
        updateMember(id, req.pw(), req.name(), req.phone());

        Student student = getStudentByMember(service.getMemberBy(id));
        student.updateInfo(req.grade(), req.room(), req.number());
        student.updateParentPhone(req.parentPhone());
        return Response.noContent("학생 정보 수정 성공");
    }

    public Response updateTeacherForAdmin(String id, UpdateTeacherForAdminReq req){
        updateMember(id, req.pw(), req.name(), req.phone());

        Teacher teacher = service.getTeacherBy(service.getMemberBy(id));
        teacher.updateInfo(req.tel(), req.position());
        return Response.noContent("선생 정보 수정 성공");
    }

    private void updateMember(String id, String pw, String name, String phone) {
        Member member = service.getMemberBy(id);
        member.updateInfoForAdmin(encodePw(pw), name, phone);
        service.save(member);
    }

    private Student getStudentByMember(Member member) {
        return service.getStudentByMember(member)
                .orElseThrow(StudentNotFoundException::new);
    }

}
