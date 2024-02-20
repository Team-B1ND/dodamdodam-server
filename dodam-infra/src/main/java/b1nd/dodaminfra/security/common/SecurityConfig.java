package b1nd.dodaminfra.security.common;

import b1nd.dodaminfra.token.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

    private final TokenFilter tokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()

                .and()
                .csrf().disable()
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests()
                
                .requestMatchers(POST, "/auth/**").permitAll()

                .requestMatchers(POST, "/member/**").permitAll()
          
                .requestMatchers(GET, "/bus").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/bus/apply/**").hasRole("STUDENT")
                .requestMatchers("/bus/**").hasRole("TEACHER")

                .requestMatchers(POST, "/night-study").hasRole("STUDENT")
                .requestMatchers(DELETE, "/night-study/**").hasRole("STUDENT")
                .requestMatchers(GET, "/night-study/my").hasRole("STUDENT")
                .requestMatchers("/night-study/**").hasAnyRole("TEACHER", "ADMIN")

                .requestMatchers(POST, "/out-going").hasRole("STUDENT")
                .requestMatchers(DELETE, "/out-going/**").hasRole("STUDENT")
                .requestMatchers(GET, "/out-going/my").hasRole("STUDENT")
                .requestMatchers("/out-going/**").hasAnyRole("TEACHER", "ADMIN")

                .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}