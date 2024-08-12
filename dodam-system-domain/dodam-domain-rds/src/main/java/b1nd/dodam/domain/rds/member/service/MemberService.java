package b1nd.dodam.domain.rds.member.service;

import b1nd.dodam.domain.rds.member.entity.BroadcastClubMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.exception.MemberNotFoundException;
import b1nd.dodam.domain.rds.member.exception.StudentNotFoundException;
import b1nd.dodam.domain.rds.member.exception.TeacherNotFoundException;
import b1nd.dodam.domain.rds.member.repository.BroadcastClubMemberRepository;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
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

    public List<Member> searchByName(String name) {
        return memberRepository.findByNameContains(name);
    }

    public List<Member> getByStatus(ActiveStatus status) {
        return memberRepository.findByStatus(status);
    }

    public Optional<Student> getStudentByMember(Member member) {
        return studentRepository.findByMember(member);
    }

    public Student getStudentBy(Member member) {
        return studentRepository.findByMember(member)
                .orElseThrow(StudentNotFoundException::new);
    }

    public Student getStudentBy(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }

    public List<Student> getStudentsByIds(List<Integer> ids) {
        return studentRepository.getByIds(ids);
    }

    public Optional<Teacher> getTeacherOrNullByMember(Member member) {
        return teacherRepository.findByMember(member);
    }

    public Teacher getTeacherBy(Member member) {
        return teacherRepository.findByMember(member)
                .orElseThrow(TeacherNotFoundException::new);
    }

}
