package b1nd.dodam.google.smtp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SMTPClient {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String targetEmail, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(targetEmail);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }
}
