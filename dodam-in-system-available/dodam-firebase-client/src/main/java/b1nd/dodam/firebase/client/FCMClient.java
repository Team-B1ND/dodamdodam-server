package b1nd.dodam.firebase.client;

import b1nd.dodam.core.exception.global.InternalServerException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMClient {
    public void sendMessage(PushAlarmEvent pushAlarmEvent) {
        try {
            if(!pushAlarmEvent.pushToken().isBlank()){
                FirebaseMessaging.getInstance().send(Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(pushAlarmEvent.title())
                                .setBody(pushAlarmEvent.body())
                                .build())
                        .setToken(pushAlarmEvent.pushToken())
                        .build());
            }
        } catch (FirebaseMessagingException e){
            throw new InternalServerException();
        }
    }
}
