package b1nd.dodamapi.point.handler;

import b1nd.dodamcore.member.domain.event.StudentRegisteredEvent;
import b1nd.dodamcore.point.application.PointService;
import b1nd.dodamcore.point.domain.entity.PointScore;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PointEventHandler {

    private final PointService service;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void listen(StudentRegisteredEvent e) {
        service.save(PointScore.builder()
                .student(e.student())
                .build()
        );
    }

}
