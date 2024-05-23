package b1nd.dodaminfra.cloud.fcm;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.pushmessage.application.FCMSender;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class FirebaseMessageSender implements FCMSender {

    @Override
    public void sendToMemberList(List<Member> memberList, String title, String body) {
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

    @Override
    public void sendToMember(Member member, String title, String body) {
        String pushToken = member.getPushToken();
        if (pushToken == null) {
            return ;
        }
        Notification notification = buildNotification(title, body);
        Message message = buildMessage(pushToken, notification);

        try {
            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getPushTokenByMemberList(List<Member> memberList) {
        return memberList.stream()
                .map(Member::getPushToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Notification buildNotification(String title, String body) {
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
                .build()).collect(Collectors.toList());
    }

    private Message buildMessage(String pushToken, Notification notification) {
        return Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .setNotification(notification)
                .setToken(pushToken)
                .build();
    }
}
