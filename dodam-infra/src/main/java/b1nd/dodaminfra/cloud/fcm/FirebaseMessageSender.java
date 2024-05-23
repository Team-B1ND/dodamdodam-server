package b1nd.dodaminfra.cloud.fcm;

import b1nd.dodamcore.member.domain.entity.Member;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Component
public class FirebaseMessageSender {
    public void sendByMemberList(List<Member> memberList, String title, String body){
        List<String> pushTokenList = getPushTokenByMemberList(memberList);
        Notification notification = buildNotification(title, body);
        List<Message> messages = buildMessageList(pushTokenList, notification);

        try {
            // 알림 발송
            FirebaseMessaging.getInstance().sendEachAsync(messages).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getPushTokenByMemberList(List<Member> member){
        return member
                .stream()
                .map(Member::getPushToken)
                .filter(Objects::nonNull)
                .toList();
    }

    private Notification buildNotification(String title, String body){
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private List<Message> buildMessageList(List<String> pushTokenList, Notification notification) {
        return pushTokenList.stream().map(token -> Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .setNotification(notification)
                .setToken(token)
                .build()).toList();
    }
}
