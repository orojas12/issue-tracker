package dev.oscarrojas.issuetracker.config;

import dev.oscarrojas.issuetracker.auth.HttpStatusAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http.cors(Customizer.withDefaults())
                .addFilterBefore(new CsrfCookieFilter(), UsernamePasswordAuthenticationFilter.class)
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
     */

    @Order(1)
    @Bean
    SecurityFilterChain securityFilterChainDefault(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .cors((cors) -> cors
                .configurationSource(corsConfigurationSource())
            )
            .csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173", "http://localhost:5173"));
        cors.setAllowedMethods(Arrays.asList("*"));
        cors.setAllowedHeaders(Arrays.asList("Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationEntryPointFailureHandler(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }
    
    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new HttpStatusAuthenticationSuccessHandler(HttpStatus.OK);
    }
}
