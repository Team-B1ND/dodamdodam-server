package b1nd.dodaminfra.cloud;

import b1nd.dodaminfra.cloud.data.CredentialsData;
import b1nd.dodaminfra.cloud.data.RegionData;
import b1nd.dodaminfra.cloud.data.StorageData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class CloudProperties {

    @Bean
    @ConfigurationProperties("cloud.aws.credentials")
    public CredentialsData credentials() {
        return new CredentialsData();
    }

    @Bean
    @ConfigurationProperties("cloud.aws.region")
    public RegionData region() {
        return new RegionData();
    }

    @Bean
    @ConfigurationProperties("cloud.aws.storage")
    public StorageData storage() {
        return new StorageData();
    }
}
