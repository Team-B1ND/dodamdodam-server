package b1nd.dodam;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class DodamRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DodamRestApiApplication.class, args);
    }

}
