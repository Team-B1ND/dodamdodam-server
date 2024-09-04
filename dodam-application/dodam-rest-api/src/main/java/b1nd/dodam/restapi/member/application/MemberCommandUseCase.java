package b1nd.dodam.restapi.member.application;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.member.event.StudentRegisteredEvent;
import b1nd.dodam.domain.rds.member.exception.*;
import b1nd.dodam.domain.rds.member.repository.BroadcastClubMemberRepository;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.member.application.data.req.*;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.encrypt.Sha512PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberCommandUseCase {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BroadcastClubMemberRepository broadcastClubMemberRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final ApplicationEventPublisher eventPublisher;

    public Response join(JoinStudentReq req) {
        checkIfIdIsDuplicate(req.id());
        Member member = memberRepository.save(req.mapToMember(encodePw(req.pw())));
        Student student = studentRepository.save(req.mapToStudent(member));
        publishStudentRegisteredEvent(student);
        return Response.created("학생 회원가입 성공");
    }

    private void publishStudentRegisteredEvent(Student student) {
        eventPublisher.publishEvent(new StudentRegisteredEvent(student));
    }

    public Response join(JoinTeacherReq req) {
        checkIfIdIsDuplicate(req.id());
        Member member = memberRepository.save(req.mapToMember(encodePw(req.pw())));
        teacherRepository.save(req.mapToTeacher(member));
        return Response.created("선생님 회원가입 성공");
    }

    private void checkIfIdIsDuplicate(String id) {
        if(memberRepository.existsById(id)) {
            throw new MemberDuplicateException();
        }
    }

    private String encodePw(String rawPw) {
        return Sha512PasswordEncoder.encode(rawPw);
    }

    public Response apply(ApplyBroadcastClubMemberReq req) {
        Member member = memberRepository.getById(req.id());
        checkIfMemberIsAlreadyBroadcastClubMember(member);
        broadcastClubMemberRepository.save(req.toEntity(member));
        return Response.created("방송부원 등록 성공");
    }

    private void checkIfMemberIsAlreadyBroadcastClubMember(Member member) {
        if(broadcastClubMemberRepository.existsByMember(member)) {
            throw new BroadcastClubMemberDuplicateException();
        }
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response delete(String id) {
        Member member = memberRepository.getById(id);
        member.checkIfStatusIsActive();
        memberRepository.delete(member);
        return Response.noContent("멤버 삭제 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response status(String id, ActiveStatus status) {
        Member member = memberRepository.getById(id);
        member.updateStatus(status);
        return Response.ok("멤버 상태변경 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response active(String id) {
        updateStatus(id, ActiveStatus.ACTIVE);
        return Response.ok("멤버 활성화 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response deactivate(String id) {
        updateStatus(id, ActiveStatus.DEACTIVATE);
        return Response.ok("멤버 비활성화 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response deactivate() {
        Member member = memberAuthenticationHolder.current();
        member.updateStatus(ActiveStatus.DEACTIVATED);
        memberRepository.save(member);
        return Response.ok("멤버 비활성화 성공");
    }

    private void updateStatus(String id, ActiveStatus status) {
        Member member = memberRepository.getById(id);
        member.updateStatus(status);
    }

    public Response updatePassword(UpdatePasswordReq req) {
        Member member = memberAuthenticationHolder.current();
        member.updatePw(Sha512PasswordEncoder.encode(req.password()));
        memberRepository.save(member);
        return Response.noContent("비밀번호 수정 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response updateMemberInfo(UpdateMemberInfoReq req) {
        Member member = memberAuthenticationHolder.current();
        member.updateInfo(req.name(), req.email(), req.phone(), req.profileImage());
        memberRepository.save(member);
        return Response.noContent("내 정보 수정 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response updateStudentInfo(UpdateStudentInfoReq req) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        student.updateInfo(req.grade(), req.room(), req.number());
        return Response.noContent("내 학생 정보 수정 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response updateStudentParentPhone(String id, UpdateStudentForAdminReq req){
        Member member = memberRepository.getById(id);
        Student student = studentRepository.getByMember(member);
        student.updateParentPhone(req.parentPhone());
        return Response.noContent("학생 정보 수정 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response updateTeacherForAdmin(String id, UpdateTeacherForAdminReq req){
        Member member = memberRepository.getById(id);
        Teacher teacher = teacherRepository.getByMember(member);
        teacher.updateInfo(req.tel(), req.position());
        return Response.noContent("선생 정보 수정 성공");
    }

}
