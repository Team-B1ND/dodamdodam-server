package b1nd.dodam.restapi.division.presentation;

import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.event.JoinMemberToDivisionEvent;
import b1nd.dodam.domain.rds.division.repository.DivisionMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DivisionEventHandler {
    private final DivisionMemberRepository divisionMemberRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void listen(JoinMemberToDivisionEvent e) {
        divisionMemberRepository.save(DivisionMember.builder()
                .permission(e.permission())
                .status(e.status())
                .division(e.division())
                .member(e.member())
                .build()
        );
    }
}
