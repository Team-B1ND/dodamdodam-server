package b1nd.dodamcore.member.application;

import b1nd.dodamcore.member.domain.entity.*;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.exception.MemberNotFoundException;
import b1nd.dodamcore.member.domain.exception.StudentNotFoundException;
import b1nd.dodamcore.member.domain.exception.TeacherNotFoundException;
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
    private final MemberSessionHolder sessionHolder;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public BroadcastClubMember save(BroadcastClubMember broadcastClubMember) {
        return broadcastClubMemberRepository.save(broadcastClubMember);
    }

    public void delete(Member member) {
        studentRepository.deleteByMember(member);
        teacherRepository.deleteByMember(member);
        memberRepository.delete(member);
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

    public Member getMemberBy(String id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member getMemberFromSession() {
        return sessionHolder.current();
    }

    public List<Member> searchByName(String name) {
        return memberRepository.findByNameContains(name);
    }

    public List<Member> getByStatus(AuthStatus status) {
        return memberRepository.findByStatus(status);
    }

    public Optional<Student> getStudentByMember(Member member) {
        return studentRepository.findByMember(member);
    }

    public Student getStudentBy(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }

    public Student getStudentFromSession() {
        return studentRepository.findByMember(sessionHolder.current())
                .orElseThrow(StudentNotFoundException::new);
    }

    public List<Student> getStudentsByIds(List<Integer> ids) {
        return studentRepository.getByIds(ids);
    }

    public Optional<Teacher> getTeacherOrNullByMember(Member member) {
        return teacherRepository.findByMember(member);
    }

    public Teacher getTeacherFromSession() {
        return teacherRepository.findByMember(sessionHolder.current())
                .orElseThrow(TeacherNotFoundException::new);
    }

}
