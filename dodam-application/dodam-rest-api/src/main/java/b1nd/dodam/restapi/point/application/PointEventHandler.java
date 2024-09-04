package b1nd.dodam.restapi.point.application;

import b1nd.dodam.domain.rds.member.event.StudentRegisteredEvent;
import b1nd.dodam.domain.rds.point.entity.PointScore;
import b1nd.dodam.domain.rds.point.event.PointSMSEvent;
import b1nd.dodam.domain.rds.point.repository.PointScoreRepository;
import b1nd.dodam.gabia.client.GabiaSMSClient;
import b1nd.dodam.gabia.client.data.req.SendSmsReq;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PointEventHandler {

    private final PointScoreRepository pointScoreRepository;
    private final GabiaSMSClient gabiaSMSClient;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void listen(StudentRegisteredEvent e) {
        pointScoreRepository.save(PointScore.builder()
                .student(e.student())
                .build()
        );
    }

    @Async
    @TransactionalEventListener
    public void listen(PointSMSEvent e) {
        gabiaSMSClient.send(new SendSmsReq(e.content(), e.phone()));
        gabiaSMSClient.send(new SendSmsReq(e.content(), e.parentPhone()));
    }

}
