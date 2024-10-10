package b1nd.dodam.neis.client.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.neis")
public class NeisCoreProperties {

    private String apiKey;
    private String url;

}
