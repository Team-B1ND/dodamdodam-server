package b1nd.dodaminfra.cloud.fcm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("cloud.firebase.key")
public class FirebaseProperties {
    private String scope;
    private String path;
}
