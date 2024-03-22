package b1nd.dodamcore.member.application;

import b1nd.dodamcore.member.domain.entity.*;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BroadcastClubMemberRepository broadcastClubMemberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Student save(Student student) {
        memberRepository.save(student.getMember());
        return studentRepository.save(student);
    }

    public Teacher save(Teacher teacher) {
        memberRepository.save(teacher.getMember());
        return teacherRepository.save(teacher);
    }

    public BroadcastClubMember save(BroadcastClubMember broadcastClubMember) {
        return broadcastClubMemberRepository.save(broadcastClubMember);
    }

    public boolean checkIdDuplication(String id) {
        return memberRepository.existsById(id);
    }

    public boolean checkBroadcastClubMember(Member member) {
        return broadcastClubMemberRepository.existsByMember(member);
    }

    public Optional<Member> getMemberById(String id) {
        return memberRepository.findById(id);
    }

    public Optional<Student> getStudentByMember(Member member) {
        return studentRepository.findByMember(member);
    }

    public Optional<Teacher> getTeacherByMember(Member member) {
        return teacherRepository.findByMember(member);
    }

    public List<Member> searchByName(String name) {
        return memberRepository.findByNameContains(name);
    }

    public List<Member> getByStatus(AuthStatus status) {
        return memberRepository.findByStatus(status);
    }

}