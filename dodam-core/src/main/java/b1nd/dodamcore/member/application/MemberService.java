package b1nd.dodamcore.member.application;

import b1nd.dodamcore.member.repository.MemberRepository;
import b1nd.dodamcore.member.repository.StudentRepository;
import b1nd.dodamcore.member.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
}
