package b1nd.dodam.neis.meal.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.neis.meal")
public class NeisMealProperties {

    private String mealEndpoint;

}
