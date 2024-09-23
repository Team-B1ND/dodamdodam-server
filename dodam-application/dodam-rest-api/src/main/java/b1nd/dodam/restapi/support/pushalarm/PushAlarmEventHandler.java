package b1nd.dodam.restapi.support.pushalarm;

import b1nd.dodam.firebase.client.FCMClient;
import b1nd.dodam.firebase.client.PushAlarmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PushAlarmEventHandler {
    private final FCMClient fcmClient;

    @Async
    @TransactionalEventListener
    public void listen(PushAlarmEvent e){
        fcmClient.sendMessage(e.pushToken(), e.title(), e.body());
    }
}
