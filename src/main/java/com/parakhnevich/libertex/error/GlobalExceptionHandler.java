package com.parakhnevich.libertex.error;

import com.parakhnevich.libertex.error.exception.NotEnoughFundsException;
import com.parakhnevich.libertex.error.exception.NotFoundException;
import com.parakhnevich.libertex.error.exception.TransactionAlreadyProcessedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handler for all exceptions not handled by other handlers
     *
     * @return response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logExceptionMessage(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Something went wrong. Try it again later.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return createResponseEntity(errorResponse);
    }

    @ExceptionHandler(NotEnoughFundsException.class)
    public ResponseEntity<ErrorResponse> handleException(NotEnoughFundsException ex) {
        logExceptionMessage(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();
        return createResponseEntity(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        logExceptionMessage(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return createResponseEntity(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(TransactionAlreadyProcessedException ex) {
        logExceptionMessage(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();
        return createResponseEntity(errorResponse);
    }

    private void logExceptionMessage(Exception e) {
        log.warn("Caught exception while handling a request: {}", e.getMessage());
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

}
