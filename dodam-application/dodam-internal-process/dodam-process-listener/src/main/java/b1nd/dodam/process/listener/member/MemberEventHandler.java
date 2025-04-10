package b1nd.dodam.process.listener.member;

import b1nd.dodam.domain.rds.member.event.MemberSMSEvent;
import b1nd.dodam.gabia.client.GabiaSMSClient;
import b1nd.dodam.gabia.client.data.req.SendSmsReq;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberEventHandler {

    private final GabiaSMSClient gabiaSMSClient;

    @Async
    @TransactionalEventListener
    public void listen(MemberSMSEvent e) {
        gabiaSMSClient.send(new SendSmsReq(e.content(), e.phone()));
    }

}
