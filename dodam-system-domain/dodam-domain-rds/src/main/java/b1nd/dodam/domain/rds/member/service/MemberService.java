package b1nd.dodam.domain.rds.member.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.StudentRelation;
import b1nd.dodam.domain.rds.member.exception.CodeNotFoundException;
import b1nd.dodam.domain.rds.member.repository.StudentRelationRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final StudentRepository studentRepository;

    public Student checkCode(String code){
        return studentRepository.findByCode(code)
                .orElseThrow(CodeNotFoundException::new);
    }

}
