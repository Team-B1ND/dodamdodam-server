package b1nd.dodamcore.member.application;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.repository.MemberRepository;
import b1nd.dodamcore.member.repository.StudentRepository;
import b1nd.dodamcore.member.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public Member getById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

}