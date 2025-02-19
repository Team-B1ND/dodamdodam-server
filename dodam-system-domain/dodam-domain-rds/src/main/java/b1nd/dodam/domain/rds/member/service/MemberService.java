package b1nd.dodam.domain.rds.member.service;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.exception.CodeNotFoundException;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.service.support.MemberMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final StudentRepository studentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Student checkCode(String code){
        return studentRepository.findByCode(code)
                .orElseThrow(CodeNotFoundException::new);
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
