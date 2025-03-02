package b1nd.dodam.restapi.bus.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.qr-code")
public class BusQRCodeProperties {
    private String apiKey;
}
