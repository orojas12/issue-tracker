package dev.oscarrojas.issuetracker.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpStatusAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final HttpStatus httpStatus;

    public HttpStatusAuthenticationSuccessHandler(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                response.setStatus(httpStatus.value());
    }
    
}
