package app.ishiko.server.auth;

public class BadCredentialsExceptionHandler
        extends DefaultAuthenticationExceptionHandler {

    public BadCredentialsExceptionHandler() {
        super();
        this.errorMessage = "Invalid username or password";
    }
}
