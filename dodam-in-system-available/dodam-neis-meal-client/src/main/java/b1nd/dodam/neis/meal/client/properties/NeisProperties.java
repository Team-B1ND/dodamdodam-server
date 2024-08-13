package b1nd.dodam.neis.meal.client.properties;

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
