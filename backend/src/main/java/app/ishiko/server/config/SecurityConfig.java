package app.ishiko.server.config;

import app.ishiko.server.auth.BadCredentialsExceptionHandler;
import app.ishiko.server.auth.DefaultAuthenticationExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static RSAPublicKey readX509PublicKey(File file)
            throws IOException {
        try (var keyReader = new FileReader(file)) {
            var pemParser = new PEMParser(keyReader);
            var converter = new JcaPEMKeyConverter();
            SubjectPublicKeyInfo publicKeyInfo =
                    SubjectPublicKeyInfo.getInstance(pemParser.readObject());
            return (RSAPublicKey) converter.getPublicKey(publicKeyInfo);
        }
    }

    private static RSAPrivateKey readPKCS8PrivateKey(File file)
            throws IOException {
        try (var keyReader = new FileReader(file)) {
            var pemParser = new PEMParser(keyReader);
            var converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo =
                    PrivateKeyInfo.getInstance(pemParser.readObject());
            return (RSAPrivateKey) converter.getPrivateKey(privateKeyInfo);
        }
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {
        var authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher =
                authorizationServerConfigurer.getEndpointsMatcher();

        // TODO: if user navigates directly to /api/auth/signin, redirect
        // to client oidc login endpoint

        http.securityMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher("/oauth2/authorize"),
                endpointsMatcher)).cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher(
                                HttpMethod.OPTIONS.name()))
                        .permitAll().anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); // Enable OpenID Connect 1.0

        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(
                                        "/auth/signin"),
                                new MediaTypeRequestMatcher(
                                        MediaType.TEXT_HTML)))
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http) throws Exception {
        http.securityMatcher(new AntPathRequestMatcher("/resource/**"))
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain1(HttpSecurity http,
            @Value("${ishiko.base_url}") String baseUrl, ObjectMapper mapper)
            throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/css/**", "/js/**", "/favicon.ico")
                        .permitAll().requestMatchers("/error").permitAll()
                        .requestMatchers("/auth/*").permitAll().anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin.loginPage("/auth/signin")
                        .loginProcessingUrl("/auth/signin")
                        .failureHandler(authenticationFailureHandler())
                        .defaultSuccessUrl(baseUrl));
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        LinkedHashMap<Class<? extends AuthenticationException>, AuthenticationFailureHandler> handlers =
                new LinkedHashMap<>();
        handlers.put(BadCredentialsException.class,
                new BadCredentialsExceptionHandler());
        return new DelegatingAuthenticationFailureHandler(handlers,
                new DefaultAuthenticationExceptionHandler());
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

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        UserDetailsManager userDetailsManager =
                new JdbcUserDetailsManager(dataSource);

        // TODO: remove before deploying
        if (!userDetailsManager.userExists("oscar")) {
            UserDetails userDetails =
                    User.withDefaultPasswordEncoder().username("oscar")
                            .password("password").roles("USER").build();
            userDetailsManager.createUser(userDetails);
        }
        return userDetailsManager;
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            JdbcTemplate jdbcTemplate,
            @Value("${ishiko.base_url}") String baseUrl) {
        var registeredClientRepository =
                new JdbcRegisteredClientRepository(jdbcTemplate);
        RegisteredClient registeredClient = RegisteredClient.withId("client1")
                .clientId("ishiko-client").clientSecret("{noop}secret")
                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(
                        AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(baseUrl + "/oidc/code")
                .postLogoutRedirectUri(baseUrl + "/oidc/post-logout")
                .scopes(scopes -> scopes.addAll(List.of(OidcScopes.OPENID,
                        OidcScopes.PROFILE, "messages")))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofDays(7))
                        .refreshTokenTimeToLive(Duration.ofDays(10))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(true).build())
                .build();
        registeredClientRepository.save(registeredClient);
        return registeredClientRepository;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(
            @Value("#{environment.PUBLIC_KEY_PATH}") String publicKeyPath,
            @Value("#{environment.PRIVATE_KEY_PATH}") String privateKeyPath,
            @Value("${oauth2.authorization-server.keyid}") String keyId)
            throws IOException {
        RSAPublicKey publicKey = readX509PublicKey(new File(publicKeyPath));
        RSAPrivateKey privateKey =
                readPKCS8PrivateKey(new File(privateKeyPath));
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey)
                .keyID(keyId).build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(
            @Value("${ishiko.base_url}") String baseUrl) {
        return AuthorizationServerSettings.builder().issuer(baseUrl)
                .authorizationEndpoint("/oauth2/authorize")
                .jwkSetEndpoint("/.well-known/jwks.json")
                .tokenEndpoint("/oauth2/token")
                .oidcClientRegistrationEndpoint("/oidc/register")
                .oidcLogoutEndpoint("/oidc/logout")
                .oidcUserInfoEndpoint("/oidc/user-info")
                .tokenRevocationEndpoint("/oauth2/revoke").build();
    }
}
