package b1nd.dodam.firebase.client;

import b1nd.dodam.core.exception.global.InternalServerException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMClient {
    public void sendMessage(String pushToken, String title, String body) {
        try {
            if (StringUtils.isNotEmpty(pushToken)) {
                FirebaseMessaging.getInstance().send(Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .setToken(pushToken)
                        .build());
            }
        } catch (FirebaseMessagingException e){
            throw new InternalServerException();
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
                ).toList();
        FirebaseMessaging.getInstance().sendEachAsync(messages);
    }
}
