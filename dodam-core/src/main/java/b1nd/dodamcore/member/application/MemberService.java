package b1nd.dodamcore.member.application;

import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.member.application.dto.request.StudentJoinRequest;
import b1nd.dodamcore.member.application.dto.request.TeacherJoinRequest;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.exception.MemberDuplicateException;
import b1nd.dodamcore.member.repository.MemberRepository;
import b1nd.dodamcore.member.repository.StudentRepository;
import b1nd.dodamcore.member.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public Member getById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void joinStudent(StudentJoinRequest studentJoinRequest) {

        String encryptedPassword = checkExistMember(studentJoinRequest.id(), studentJoinRequest.pw());

        Member savedMember = memberRepository.save(studentJoinRequest.mapToMember(encryptedPassword));
        studentRepository.save(studentJoinRequest.mapToStudent(savedMember));
    }

    @Transactional
    public void joinTeacher(TeacherJoinRequest teacherJoinRequest) {

        String encryptedPassword = checkExistMember(teacherJoinRequest.id(), teacherJoinRequest.pw());

        Member savedMember = memberRepository.save(teacherJoinRequest.mapToMember(encryptedPassword));
        teacherRepository.save(teacherJoinRequest.mapToTeacher(savedMember));
    }

    private String checkExistMember(String id, String pw) {
        if (memberRepository.existsById(id)) {
            throw new MemberDuplicateException();
        }
        return passwordEncoder.encode(pw);
    }

}