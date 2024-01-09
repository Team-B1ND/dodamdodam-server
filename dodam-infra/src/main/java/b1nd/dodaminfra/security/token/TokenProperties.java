package b1nd.dodaminfra.security.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.token")
class TokenProperties {

    private String generate;
    private String verify;
    private String refresh;

}