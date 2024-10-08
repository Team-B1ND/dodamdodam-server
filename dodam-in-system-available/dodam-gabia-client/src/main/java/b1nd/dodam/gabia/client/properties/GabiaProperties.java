package b1nd.dodam.gabia.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.gabia")
public class GabiaProperties {

    private String id;
    private String apiKey;
    private String refKey;
    private String tokenUrl;
    private String smsUrl;
    private String lmsUrl;
    private String sender;
    private String subject;

}
