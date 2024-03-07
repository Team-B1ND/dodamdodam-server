package b1nd.dodaminfra.security.common;

import b1nd.dodaminfra.token.TokenExceptionFilter;
import b1nd.dodaminfra.wakeupsong.WakeupSongFilter;
import b1nd.dodaminfra.token.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

    private static final String STUDENT = "STUDENT";
    private static final String TEACHER = "TEACHER";
    private static final String ADMIN = "ADMIN";

    private final TokenFilter tokenFilter;
    private final TokenExceptionFilter tokenExceptionFilter;
    private final WakeupSongFilter wakeupSongFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()

                .and()
                .csrf().disable()
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenExceptionFilter, TokenFilter.class)
                .addFilterAfter(wakeupSongFilter, AuthorizationFilter.class)

                .authorizeHttpRequests()
                
                .requestMatchers(POST, "/auth/**").permitAll()

                .requestMatchers(POST, "/member/broadcast-club-member").hasRole(ADMIN)
                .requestMatchers(POST, "/member/**").permitAll()

                .requestMatchers(GET, "/conference").permitAll()

                .requestMatchers(GET, "/meal/**").permitAll()

                .requestMatchers("/wakeup-song/**").permitAll()

                .requestMatchers(GET, "/bus").hasAnyRole(STUDENT, TEACHER)
                .requestMatchers("/bus/apply/**").hasRole(STUDENT)
                .requestMatchers("/bus/**").hasRole(TEACHER)

                .requestMatchers(POST, "/night-study").hasRole(STUDENT)
                .requestMatchers(DELETE, "/night-study/**").hasRole(STUDENT)
                .requestMatchers(GET, "/night-study/my").hasRole(STUDENT)
                .requestMatchers("/night-study/**").hasAnyRole(TEACHER, ADMIN)

                .requestMatchers(POST, "/out-going").hasRole(STUDENT)
                .requestMatchers(DELETE, "/out-going/**").hasRole(STUDENT)
                .requestMatchers(GET, "/out-going/my").hasRole(STUDENT)
                .requestMatchers("/out-going/**").hasAnyRole(TEACHER, ADMIN)

                .requestMatchers(POST, "/out-sleeping").hasRole(STUDENT)
                .requestMatchers(DELETE, "/out-sleeping/**").hasRole(STUDENT)
                .requestMatchers(GET, "/out-sleeping/my").hasRole(STUDENT)
                .requestMatchers("/out-sleeping/**").hasAnyRole(TEACHER, ADMIN)

                .requestMatchers(GET, "/point/my/**").hasRole(STUDENT)
                .requestMatchers(GET, "/point/score/my/**").hasRole(STUDENT)
                .requestMatchers("/point/**").hasAnyRole(TEACHER, ADMIN)

                .requestMatchers(POST, "/schedule").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(PATCH, "/schedule/**").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(DELETE, "/schedule/**").hasAnyRole(TEACHER, ADMIN)

                .requestMatchers(POST, "/banner").hasRole(ADMIN)
                .requestMatchers(PATCH, "/banner/**").hasRole(ADMIN)
                .requestMatchers(DELETE, "/banner/**").hasRole(ADMIN)

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