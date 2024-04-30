package dev.oscarrojas.issuetracker.auth;

public class BadCredentialsExceptionHandler
        extends DefaultAuthenticationExceptionHandler {

    public BadCredentialsExceptionHandler() {
        super();
        this.errorMessage = "Invalid username or password";
    }
}
