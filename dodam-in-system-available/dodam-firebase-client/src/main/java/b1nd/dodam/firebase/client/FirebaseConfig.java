package b1nd.dodam.firebase.client;

import b1nd.dodam.core.exception.global.InternalServerException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init(){
        try{
            InputStream serviceAccount = new ClassPathResource("dodamdodam-firebase-key.json").getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e){
            log.error("error cause: {}, error message", e.getCause(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

