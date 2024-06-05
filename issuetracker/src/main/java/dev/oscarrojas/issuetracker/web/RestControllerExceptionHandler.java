package dev.oscarrojas.issuetracker.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.oscarrojas.issuetracker.exceptions.InvalidInputException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = { RestController.class })
public class RestControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public RestExceptionResponse handleNotFound(HttpServletRequest request, NotFoundException e) {
        return new RestExceptionResponse(request.getRequestURL().toString(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    @ResponseBody
    public RestExceptionResponse handleInvalidInput(HttpServletRequest request, InvalidInputException e) {
        return new RestExceptionResponse(request.getRequestURL().toString(), e.getMessage());
    }

    static record RestExceptionResponse(String url, String error) {
    }
}
