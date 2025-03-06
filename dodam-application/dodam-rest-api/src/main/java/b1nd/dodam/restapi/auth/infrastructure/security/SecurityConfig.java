package b1nd.dodam.restapi.auth.infrastructure.security;

import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import b1nd.dodam.restapi.auth.infrastructure.security.filter.BroadcastMemberFilter;
import b1nd.dodam.restapi.auth.infrastructure.security.filter.DivisionPermissionInterceptor;
import b1nd.dodam.restapi.auth.infrastructure.security.filter.TokenExceptionFilter;
import b1nd.dodam.restapi.auth.infrastructure.security.filter.TokenFilter;
import b1nd.dodam.restapi.support.exception.ErrorResponseSender;
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
    private static final String PARENT = "PARENT";
    private static final String ADMIN = "ADMIN";

    private final TokenFilter tokenFilter;
    private final TokenExceptionFilter tokenExceptionFilter;
    private final BroadcastMemberFilter broadcastMemberFilter;
    private final DivisionPermissionInterceptor divisionPermissionFilter;
    private final ErrorResponseSender errorResponseSender;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()

                .and()
                .csrf().disable()
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenExceptionFilter, TokenFilter.class)
                .addFilterAfter(broadcastMemberFilter, AuthorizationFilter.class)

                .authorizeHttpRequests()

                .requestMatchers("/actuator/prometheus").permitAll()
                
                .requestMatchers(POST, "/auth/**").permitAll()

                .requestMatchers(POST, "/member/broadcast-club-member").hasRole(ADMIN)
                .requestMatchers(POST, "/member/relation").hasAnyRole(PARENT)
                .requestMatchers(POST, "/member/**").permitAll()
                .requestMatchers(GET, "/member/my").authenticated()
                .requestMatchers(GET, "/member/check/broadcast-club-member").hasAnyRole(STUDENT, ADMIN)
                .requestMatchers(GET, "/member/code/**").permitAll()
                .requestMatchers(GET, "/member/relation").hasAnyRole(PARENT)
                .requestMatchers(GET, "/member/**").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(PATCH, "/member/student/info/**").hasAnyRole(STUDENT, ADMIN)
                .requestMatchers(PATCH, "/member/teacher/info/**").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(PATCH, "/member/status/**").hasRole(ADMIN)
                .requestMatchers(PATCH, "/member/active/**").hasRole(ADMIN)
                .requestMatchers(PATCH, "/member/deactivate").authenticated()
                .requestMatchers(PATCH, "/member/deactivate/**").hasRole(ADMIN)
                .requestMatchers(PATCH, "/member/student/info").hasRole(STUDENT)
                .requestMatchers(PATCH, "/member/**").authenticated()

                .requestMatchers(GET, "/conference").permitAll()

                .requestMatchers(GET, "/meal/**").permitAll()

                .requestMatchers("/wakeup-song/**").permitAll()

                .requestMatchers(GET, "/bus").permitAll()
                .requestMatchers("/bus/apply/**").hasAnyRole(STUDENT, ADMIN)
                .requestMatchers("/bus/**").hasRole(TEACHER)
                .requestMatchers("/bus/qr-code/**").hasAnyRole(STUDENT, ADMIN)
                .requestMatchers("/bus/qr-code/scan").permitAll()

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

                .requestMatchers(GET, "/banner/{id}", "/banner/active").permitAll()
                .requestMatchers(GET, "/banner").hasRole(ADMIN)
                .requestMatchers(POST, "/banner").hasRole(ADMIN)
                .requestMatchers(PATCH, "/banner/**").hasRole(ADMIN)
                .requestMatchers(DELETE, "/banner/**").hasRole(ADMIN)

                .requestMatchers(POST, "/recruit").hasRole(TEACHER)
                .requestMatchers(PATCH, "/recruit/**").hasRole(TEACHER)
                .requestMatchers(DELETE, "/recruit/**").hasRole(TEACHER)

                .requestMatchers(POST, "/divisions").hasAnyRole(ADMIN, TEACHER)
                .requestMatchers(POST, "/divisions/{id}/members/**").authenticated()
                .requestMatchers(GET, "/divisions/**").authenticated()
                .requestMatchers(PATCH, "/divisions/**").authenticated()
                .requestMatchers(DELETE, "/divisions/**").authenticated()

                .requestMatchers(POST, "/notice").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(PATCH, "/notice/{id}/create").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(GET, "/notice/**").authenticated()

                .requestMatchers(POST, "/clubs/state").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(POST, "/clubs/time").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(POST, "/clubs/pass").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(POST, "/clubs/*/teacher").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(GET, "/clubs/*/join-requests").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(GET, "/clubs/*/waiting").hasAnyRole(TEACHER, ADMIN)
                .requestMatchers(GET, "/clubs/join-requests/{studentId}").hasAnyRole(TEACHER, ADMIN)


                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((req, res, e) -> errorResponseSender.send(res, GlobalExceptionCode.INVALID_ROLE))
                .authenticationEntryPoint((req, res, e) -> errorResponseSender.send(res, GlobalExceptionCode.ENDPOINT_NOT_FOUND));

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
