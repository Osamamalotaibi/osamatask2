package com.example.taskapi.errors;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;

@Singleton
public class GlobalExceptionHandler
        implements ExceptionHandler<Exception, HttpResponse<Messageerrors>> {

    @Override
    public HttpResponse<Messageerrors> handle(HttpRequest request, Exception ex) {

        if (ex instanceof ConstraintViolationException cve) {
            String msg = cve.getConstraintViolations()
                    .stream()
                    .findFirst()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .orElse("Validation error");

            Messageerrors body = new Messageerrors(
                    HttpStatus.BAD_REQUEST.getCode(),
                    "Bad Request",
                    msg,
                    request.getPath()
            );
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(body);
        }

        if (ex instanceof IllegalArgumentException iae) {
            Messageerrors body = new Messageerrors(
                    HttpStatus.BAD_REQUEST.getCode(),
                    "Bad Request",
                    iae.getMessage(),
                    request.getPath()
            );
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(body);
        }

        Messageerrors body = new Messageerrors(
                HttpStatus.INTERNAL_SERVER_ERROR.getCode(),
                "Internal Server Error",
                ex.getMessage() != null ? ex.getMessage() : "Unexpected error",
                request.getPath()
        );
        return HttpResponse.serverError(body);
    }
}
