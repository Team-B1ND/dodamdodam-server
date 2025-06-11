package b1nd.dodam.firebase.client;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FCMClient {
    public boolean sendMessage(String pushToken, String title, String body) {
        if (StringUtils.isEmpty(pushToken)) return false;

        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(pushToken)
                    .build());
            return true;
        } catch (FirebaseMessagingException e){
            return false;
        }
    }


    public void sendMessages(List<String> pushTokens, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        List<Message> messages = pushTokens.stream()
                .filter(StringUtils::isNotBlank)
                .map(token -> Message.builder()
                        .setNotification(notification)
                        .setToken(token)
                        .build()
                )
                .toList();

        if (!messages.isEmpty()) {
            FirebaseMessaging.getInstance().sendEachAsync(messages);
        }
    }

}
