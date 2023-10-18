package com.LP2.EventScheduler.exception;

import com.LP2.EventScheduler.response.ErrorResponse;
import com.LP2.EventScheduler.response.ErrorValidationResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValidationResponse> handlerValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        ErrorValidationResponse errorResponse = new ErrorValidationResponse();

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        errorResponse.setErrors(errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerCategoryNotFoundException(
            CategoryNotFoundException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScheduleCreationException.class)
    public ResponseEntity<ErrorResponse> handlerScheduleCreationException(
            ScheduleCreationException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handlerNotAuthenticatedException(
            NotAuthenticatedException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerEventNotFoundException(
            EventNotFoundException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ErrorResponse> handlerInvalidJwtException(
            InvalidJwtException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FailedNotificationSendingException.class)
    public ResponseEntity<ErrorResponse> handlerFailedNotificationSendingException(
            FailedNotificationSendingException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceBelongsTheUserException.class)
    public ResponseEntity<ErrorResponse> handlerResourceBelongsTheUserException(
            ResourceBelongsTheUserException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IsNotOwnerException.class)
    public ResponseEntity<ErrorResponse> handlerIsNotOwnerException(
            IsNotOwnerException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnexpectedResourceValueException.class)
    public ResponseEntity<ErrorResponse> handlerUnexpectedResourceValueException(
            UnexpectedResourceValueException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerConnectionNotFoundException(
            ConnectionNotFoundException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<ErrorResponse> handlerResourceExistException(
            ResourceExistException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUserNotFoundException(
            UserNotFoundException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedEmailSendingException.class)
    public ResponseEntity<ErrorResponse> handlerFailedEmailSendingException(
            FailedEmailSendingException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvitationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerInvitationNotFoundException(
            InvitationNotFoundException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedImageUploadException.class)
    public ResponseEntity<ErrorResponse> handlerFailedImageUploadException(
            FailedImageUploadException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
