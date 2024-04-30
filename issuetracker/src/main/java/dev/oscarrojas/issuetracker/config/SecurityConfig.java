package dev.oscarrojas.issuetracker.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.oscarrojas.issuetracker.auth.CsrfCookieFilter;
import dev.oscarrojas.issuetracker.auth.CsrfTokenRequestHeaderHandler;
import dev.oscarrojas.issuetracker.auth.HttpStatusAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            @Value("${ishiko.base_url}") String baseUrl, ObjectMapper mapper)
            throws Exception {
        http.cors(Customizer.withDefaults())
                .addFilter(new CsrfCookieFilter())
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestHeaderHandler()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/login", "/auth/register", "/auth/csrf")
                                .permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/auth/signin")
                        .failureHandler(authenticationFailureHandler())
                        .successHandler(authenticationSuccessHandler()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${ishiko.base_url}") String baseUrl) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(baseUrl));
        configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-XSRF-TOKEN"));
        configuration.setMaxAge(600L);
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationEntryPointFailureHandler(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new HttpStatusAuthenticationSuccessHandler(HttpStatus.OK);
    }
}
