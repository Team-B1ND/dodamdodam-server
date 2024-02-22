package b1nd.dodaminfra.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.jwt")
class JwtProperties {

    private String issuer;
    private String tokenServer;

}