package b1nd.dodamcore.sms.application;

import b1nd.dodamcore.sms.application.dto.req.SendSmsReq;
import b1nd.dodamcore.sms.domain.event.SMSEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SMSEventHandler {

    private final SMSClient smsClient;

    @Async
    @TransactionalEventListener
    public void listen(SMSEvent e) {
        smsClient.send(new SendSmsReq(e.content(), e.phone()));
    }

}