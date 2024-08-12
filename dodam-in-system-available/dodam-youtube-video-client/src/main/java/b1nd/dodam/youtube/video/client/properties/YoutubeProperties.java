package b1nd.dodam.youtube.video.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.youtube.key")
public class YoutubeProperties {

    private String apiKey;

    @Bean
    @ConfigurationProperties("app.youtube.url")
    public YoutubeUrlProperties url() {
        return new YoutubeUrlProperties();
    }

}
