package fon.iot.smartplugspring.api;

import fon.iot.smartplugspring.exception.ApplicationError;
import fon.iot.smartplugspring.exception.InvalidHeaders;
import fon.iot.smartplugspring.exception.NotFoundException;
import fon.iot.smartplugspring.exception.UserAlreadyExists;
import fon.iot.smartplugspring.service.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @Value("${api_doc_url}")
    private String details;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApplicationError> handleNotFoundException(NotFoundException exception, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setCode(101);
        error.setMessage(exception.getMessage());
        error.setDetails(details);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ApplicationError> handleNotFoundException(UserAlreadyExists exception, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setCode(409);
        error.setMessage(exception.getMessage());
        error.setDetails(details);

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UnauthorizedException.class, InvalidHeaders.class})
    public ResponseEntity<ApplicationError> handleNotAuthorizedException(UnauthorizedException exception, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setCode(210);
        error.setMessage(exception.getMessage());
        error.setDetails(details);

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
