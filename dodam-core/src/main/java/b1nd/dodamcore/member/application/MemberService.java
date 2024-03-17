package b1nd.dodamcore.member.application;

import b1nd.dodamcore.member.domain.entity.*;
import b1nd.dodamcore.member.domain.exception.MemberNotFoundException;
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

    public void save(Member member, Student student) {
        memberRepository.save(member);
        studentRepository.save(student);
    }

    public void save(Member member, Teacher teacher) {
        memberRepository.save(member);
        teacherRepository.save(teacher);
    }

    public void save(BroadcastClubMember broadcastClubMember) {
        broadcastClubMemberRepository.save(broadcastClubMember);
    }

    public boolean checkIdDuplication(String id) {
        return memberRepository.existsById(id);
    }

    public boolean checkBroadcastClubMember(Member member) {
        return broadcastClubMemberRepository.existsByMember(member);
    }

    public Member getById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    public Optional<Student> getStudentByMember(Member member) {
        return studentRepository.findByMember(member);
    }

    public Optional<Teacher> getTeacherByMember(Member member) {
        return teacherRepository.findByMember(member);
    }

}