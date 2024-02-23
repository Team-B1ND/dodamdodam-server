package b1nd.dodaminfra.api.neis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.neis")
public class NeisProperties {

    private String apiKey;
    private String url;
}
