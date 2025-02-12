package b1nd.dodam.google.smtp.client;

import b1nd.dodam.core.exception.global.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SMTPClient {

    private final JavaMailSender mailSender;

    public void composeTemplate(String targetEmail, String title, int authCode){
        try {
            ClassPathResource resource = new ClassPathResource("authEmail.html");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            String templateContent = reader.lines().collect(Collectors.joining("\n"));
            String content = templateContent.replace("{authCode}", String.valueOf(authCode));
            sendEmail(targetEmail, title, content);
        }
        catch (IOException e) {
            throw new InternalServerException();
        }
    }

    @Async
    public void sendEmail(String targetEmail, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(targetEmail);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

}
