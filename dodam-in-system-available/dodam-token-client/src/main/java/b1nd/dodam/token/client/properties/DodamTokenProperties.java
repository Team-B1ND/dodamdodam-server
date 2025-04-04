package b1nd.dodam.token.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.token")
public
class DodamTokenProperties {

    private String generateAccess;
    private String generateRefresh;
    private String verify;

}
