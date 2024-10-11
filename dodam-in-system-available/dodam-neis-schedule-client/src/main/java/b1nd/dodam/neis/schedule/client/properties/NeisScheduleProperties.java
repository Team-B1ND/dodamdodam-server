package b1nd.dodam.neis.schedule.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.neis.schedule")
public class NeisScheduleProperties {

    private String scheduleEndpoint;

}
