package b1nd.dodam.restapi.group.presentation;

import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.group.event.JoinMemberToGroupEvent;
import b1nd.dodam.domain.rds.group.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class GroupEventHandler {
    private final GroupMemberRepository groupMemberRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void listen(JoinMemberToGroupEvent e) {
        groupMemberRepository.save(GroupMember.builder()
                .group(e.group())
                .member(e.member())
                .build()
        );
    }
}
