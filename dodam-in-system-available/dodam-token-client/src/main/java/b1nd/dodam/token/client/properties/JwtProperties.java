package b1nd.dodam.token.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.jwt")
public
class JwtProperties {

    private String issuer;
    private String tokenServer;

}
