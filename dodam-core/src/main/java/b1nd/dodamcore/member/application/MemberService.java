package b1nd.dodamcore.member.application;

import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.member.application.dto.req.ApplyBroadcastClubMemberReq;
import b1nd.dodamcore.member.application.dto.req.JoinStudentReq;
import b1nd.dodamcore.member.application.dto.req.JoinTeacherReq;
import b1nd.dodamcore.member.application.dto.res.MemberInfoRes;
import b1nd.dodamcore.member.domain.entity.*;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.event.StudentRegisteredEvent;
import b1nd.dodamcore.member.domain.exception.MemberDuplicateException;
import b1nd.dodamcore.member.domain.exception.MemberNotFoundException;
import b1nd.dodamcore.member.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberSessionHolder sessionHolder;
    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BroadcastClubMemberRepository broadcastClubMemberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Member getById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional(rollbackFor = Exception.class)
    public void joinStudent(JoinStudentReq studentJoinReq) {

        String encryptedPassword = checkExistMember(studentJoinReq.id(), studentJoinReq.pw());

        Member savedMember = memberRepository.save(studentJoinReq.mapToMember(encryptedPassword));
        Student student = studentRepository.save(studentJoinReq.mapToStudent(savedMember));

        publishStudentRegisteredEvent(student);
    }

    @Transactional(rollbackFor = Exception.class)
    public void joinTeacher(JoinTeacherReq teacherJoinReq) {

        String encryptedPassword = checkExistMember(teacherJoinReq.id(), teacherJoinReq.pw());

        Member savedMember = memberRepository.save(teacherJoinReq.mapToMember(encryptedPassword));
        teacherRepository.save(teacherJoinReq.mapToTeacher(savedMember));
    }

    @Transactional(rollbackFor = Exception.class)
    public void applyBroadcastClubMember(ApplyBroadcastClubMemberReq req) {
        Member member = getById(req.id());

        if(!isBroadcastClubMember(member)) {
            broadcastClubMemberRepository.save(
                    BroadcastClubMember.builder()
                            .member(member)
                            .build()
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyStatus(String id, AuthStatus status) {
        Member member = getById(id);
        member.updateStatus(status);
    }

    public MemberInfoRes getMyInfo() {
        Member member = sessionHolder.current();
        Student student = studentRepository.findByMember(member)
                .orElse(null);
        Teacher teacher = teacherRepository.findByMember(member)
                .orElse(null);

        return MemberInfoRes.of(member, student, teacher);
    }

    public List<MemberInfoRes> getAllInfo() {
        return memberRepository.findAll()
                .parallelStream()
                .map(member -> {
                            Student student = studentRepository.findByMember(member)
                                    .orElse(null);
                            Teacher teacher = teacherRepository.findByMember(member)
                                    .orElse(null);
                            return MemberInfoRes.of(member, student, teacher);
                }).toList();
    }

    public boolean isBroadcastClubMember(Member member) {
        return broadcastClubMemberRepository.existsByMember(member);
    }

    private String checkExistMember(String id, String pw) {
        if (memberRepository.existsById(id)) {
            throw new MemberDuplicateException();
        }
        return passwordEncoder.encode(pw);
    }

    private void publishStudentRegisteredEvent(Student student) {
        applicationEventPublisher.publishEvent(new StudentRegisteredEvent(student));
    }

}