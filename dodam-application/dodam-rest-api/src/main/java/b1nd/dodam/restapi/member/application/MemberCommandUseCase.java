package b1nd.dodam.restapi.member.application;

import b1nd.dodam.core.exception.global.InternalServerException;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.event.ParentRegisteredEvent;
import b1nd.dodam.domain.rds.member.event.StudentRegisteredEvent;
import b1nd.dodam.domain.rds.member.exception.*;
import b1nd.dodam.domain.rds.member.repository.*;
import b1nd.dodam.domain.rds.member.service.MemberService;
import b1nd.dodam.domain.rds.member.service.support.MemberMessageUtil;
import b1nd.dodam.domain.redis.member.enumeration.AuthType;
import b1nd.dodam.domain.redis.member.service.MemberAuthRedisService;
import b1nd.dodam.google.smtp.client.SMTPClient;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.member.application.data.req.*;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.support.encrypt.Sha512PasswordEncoder;
import b1nd.dodam.restapi.support.util.RandomCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberCommandUseCase {

    private final MemberService memberService;
    private final MemberAuthRedisService memberAuthRedisService;
    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final StudentRelationRepository studentRelationRepository;
    private final BroadcastClubMemberRepository broadcastClubMemberRepository;
    private final DormitoryManageMemberRepository dormitoryManageMemberRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final ApplicationEventPublisher eventPublisher;
    private final SMTPClient smtpClient;

    public Response join(String userAgent, JoinStudentReq req) {
        checkIfIdIsDuplicate(req.id());
        memberAuthRedisService.validateUserAuth(userAgent, true);
        Member member = memberRepository.save(req.mapToMember(encodePw(req.pw())));
        Student student = studentRepository.save(req.mapToStudent(member));
        publishStudentRegisteredEvent(student);
        return Response.created("학생 회원가입 성공");
    }

    private void publishStudentRegisteredEvent(Student student) {
        eventPublisher.publishEvent(new StudentRegisteredEvent(student));
    }

    public Response join(String userAgent, JoinTeacherReq req) {
        checkIfIdIsDuplicate(req.id());
        memberAuthRedisService.validateUserAuth(userAgent, false);
        Member member = memberRepository.save(req.mapToMember(encodePw(req.pw())));
        teacherRepository.save(req.mapToTeacher(member));
        return Response.created("선생님 회원가입 성공");
    }

    public Response join(String userAgent, JoinParentReq req) {
        checkIfIdIsDuplicate(req.id());
        memberAuthRedisService.validateUserAuth(userAgent, false);
        Member member = memberRepository.save(req.mapToMember(encodePw(req.pw())));
        Parent parent = parentRepository.save(req.mapToParent(member));
        req.relationInfo()
                .forEach(relationInfo -> connectRelation(parent.getId(), relationInfo));
        eventPublisher.publishEvent(new ParentRegisteredEvent(parent));
        return Response.created("학부모 회원가입 성공");
    }

    public Response sendAuthCode(AuthType authType, AuthCodeReq authCodeReq) {
        int authCode = RandomCode.randomCode();
        String identifier = authCodeReq.identifier();
        memberAuthRedisService.updateAuthCode(authType, identifier, authCode);
        checkType(authType, identifier, authCode);
        return ResponseData.ok("인증코드 발급 성공");
    }

    private void checkType(AuthType authType, String identifier, int authCode){
        switch (authType) {
            case EMAIL -> smtpClient.issueAuthEmail(identifier, MemberMessageUtil.createMessage(authCode), authCode);
            case PHONE -> memberService.issueAuthSMS(identifier, authCode);
            default -> throw new InternalServerException();
        }
    }

    public Response verifyAuthCode(String userAgent, AuthType authType, VerifyAuthCodeReq req) {
        memberAuthRedisService.validateAuthCode(authType, req.identifier(), req.authCode());
        memberAuthRedisService.updateUserAgentValidation(userAgent, authType, req.phone());
        return Response.ok("인증 성공");
    }

    public Response addChild(ConnectStudentReq req){
        Parent parent = parentRepository.findByMember(memberAuthenticationHolder.current());
        connectRelation(parent.getId(), req);
        return Response.ok("자녀추가 성공");
    }

    public void connectRelation(int id, ConnectStudentReq req){
        Student student = memberService.checkCode(req.code());
        Parent parent = parentRepository.findById(id)
                .orElseThrow(ParentNotFoundException::new);
        memberService.checkDuplicateStudent(student);
        studentRelationRepository.save(req.mapToStudentRelation(student, parent));
    }

    private void checkIfIdIsDuplicate(String id) {
        if (memberRepository.existsById(id)) {
            throw new MemberDuplicatedException();
        }
    }

    private String encodePw(String rawPw) {
        return Sha512PasswordEncoder.encode(rawPw);
    }

    public Response applyBroadcastClubMember(ApplyBroadcastClubMemberReq req) {
        Member member = memberRepository.getById(req.id());
        checkIfMemberIsAlreadyBroadcastClubMember(member);
        broadcastClubMemberRepository.save(req.toEntity(member));
        return Response.created("방송부원 등록 성공");
    }

    public Response removeBroadcastClubMember(ApplyBroadcastClubMemberReq req) {
        Member member = memberRepository.getById(req.id());
        checkIfMemberIsNotBroadcastClubMember(member);
        broadcastClubMemberRepository.deleteByMember(req.toEntity(member).getMember());
        return Response.ok("방송부원 삭제 성공");
    }

    private void checkIfMemberIsAlreadyBroadcastClubMember(Member member) {
        if (broadcastClubMemberRepository.existsByMember(member)) {
            throw new BroadcastClubMemberDuplicatedException();
        }
    }

    private void checkIfMemberIsNotBroadcastClubMember(Member member) {
        if (!broadcastClubMemberRepository.existsByMember(member)) {
            throw new BroadcastClubMemberNotFoundException();
        }
    }

    public Response applyDormitoryManageMember(ApplyDormitoryManageMemberReq req) {
        Member member = memberRepository.getById(req.id());
        checkIfMemberIsAlreadyDormitoryManageMember(member);
        dormitoryManageMemberRepository.save(req.toEntity(member));
        return Response.created("자치위원 등록 성공");
    }

    public Response removeDormitoryManageMember(ApplyDormitoryManageMemberReq req) {
        Member member = memberRepository.getById(req.id());
        checkIfMemberIsNotDormitoryManageMember(member);
        dormitoryManageMemberRepository.deleteByMember(req.toEntity(member).getMember());
        return Response.ok("자치위원 삭제 성공");
    }

    private void checkIfMemberIsAlreadyDormitoryManageMember(Member member) {
        if (dormitoryManageMemberRepository.existsByMember(member)) {
            throw new DormitoryManageMemberDuplicatedException();
        }
    }

    private void checkIfMemberIsNotDormitoryManageMember(Member member) {
        if (!dormitoryManageMemberRepository.existsByMember(member)) {
            throw new DormitoryManageMemberNotFoundExcpetion();
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
    public Response deactivate() {
        Member member = memberAuthenticationHolder.current();
        member.updateStatus(ActiveStatus.DEACTIVATED);
        memberRepository.save(member);
        return Response.ok("멤버 비활성화 성공");
    }

    @CacheEvict(value = "members-cache", key = "'activeMembers'")
    public Response deactivateThirdGrade(){
        List<Member> members = studentRepository.findMembersByGrade(3);
        for (Member member : members) {
            member.updateStatus(ActiveStatus.DEACTIVATED);
        }
        memberRepository.saveAll(members);
        return Response.noContent("졸업생 비활성화 성공");
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
    public Response updateTeacherForAdmin(String id, UpdateTeacherForAdminReq req){
        Member member = memberRepository.getById(id);
        Teacher teacher = teacherRepository.getByMember(member);
        teacher.updateInfo(req.tel(), req.position());
        return Response.noContent("선생 정보 수정 성공");
    }

}
