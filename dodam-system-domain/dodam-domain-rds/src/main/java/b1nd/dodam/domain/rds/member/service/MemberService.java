package b1nd.dodam.domain.rds.member.service;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.StudentRelation;
import b1nd.dodam.domain.rds.member.exception.ChildDuplicatedException;
import b1nd.dodam.domain.rds.member.exception.CodeNotFoundException;
import b1nd.dodam.domain.rds.member.repository.ParentRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRelationRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.service.support.MemberMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final StudentRelationRepository studentRelationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void checkDuplicateStudent(Student student){
        boolean isStudent = studentRelationRepository.existsByStudent(student);
        if (isStudent) throw new ChildDuplicatedException();
    }

    public Student checkCode(String code){
        return studentRepository.findByCode(code)
                .orElseThrow(CodeNotFoundException::new);
    }

    public List<StudentRelation> getStudentRelationByMember(Member member){
        Parent parent = getParentByMember(member);
        return studentRelationRepository.findByParent(parent);
    }

    private Parent getParentByMember(Member member){
        return parentRepository.findByMember(member);
    }

    public Member toggleAlarm(Member member) {
        boolean newState = Boolean.TRUE.equals(member.isAlarm()) ? Boolean.FALSE : Boolean.TRUE;
        member.setAlarm(newState);
        return member;
    }

    public void issueAuthSMS(String phone, int authCode){
        eventPublisher.publishEvent(MemberMessageUtil.createIssuedEvent(phone, authCode));
    }

}
