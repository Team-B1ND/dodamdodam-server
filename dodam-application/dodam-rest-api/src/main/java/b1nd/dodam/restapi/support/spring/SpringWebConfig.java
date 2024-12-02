package b1nd.dodam.restapi.support.spring;

import b1nd.dodam.restapi.auth.infrastructure.security.filter.DivisionPermissionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SpringWebConfig implements WebMvcConfigurer {

    private final DivisionPermissionInterceptor divisionPermissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(divisionPermissionInterceptor)
                .addPathPatterns("/divisions/{id}", "/divisions/{id}/members");
    }
}
