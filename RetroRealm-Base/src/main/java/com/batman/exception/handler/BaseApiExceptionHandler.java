package com.batman.exception.handler;

import com.batman.exception.payload.ExceptionMsg;
import com.batman.exception.wrapper.InputFieldException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@RestControllerAdvice
@Order
public class BaseApiExceptionHandler {

    @ExceptionHandler(value = { InputFieldException.class })
    public ResponseEntity<ExceptionMsg> handleValidationException(final InputFieldException e) {

        log.info("**BaseApiExceptionHandler, handle Input validation exception*\n");
        log.error(ExceptionUtils.getFullStackTrace(e));
        final var badRequest = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**").httpStatus(badRequest)
                .timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
    }

    @ExceptionHandler(value = { Throwable.class })
    public ResponseEntity<ExceptionMsg> handleAllException(final Throwable e) {

        log.info("**BaseApiExceptionHandler, handle Unknown exception*\n");
        log.error(ExceptionUtils.getFullStackTrace(e));
        final var internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
                .httpStatus(internalServerError).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
                internalServerError);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    public <T extends DataAccessException> ResponseEntity<ExceptionMsg> handleDataAccessException(final T e) {

        log.info("**BaseApiExceptionHandler, handle data access exception exception*\n");
        log.error(ExceptionUtils.getFullStackTrace(e));
        final var badRequest = HttpStatus.BAD_REQUEST;
        String errorMessage = e.getMessage();
        if(e.getMessage().contains("Duplicate")) errorMessage = "Record Already Exists";
        return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + errorMessage + "!**").httpStatus(badRequest)
                .timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
    }
}
