package com.instabook.common.exception;

import com.instabook.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * ExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public R<?> illegalArgumentException(IllegalArgumentException ex) {
        ex.printStackTrace();
        log.error("error msg:", ex);
        return R.error(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage());
    }

    @ExceptionHandler(value = {ClientException.class})
    @ResponseStatus(HttpStatus.OK)
    public R<?> commonServiceException(ClientException ex) {
        ex.printStackTrace();
        log.error("error msg:", ex);
        return R.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> unknownException(Exception ex) {
        ex.printStackTrace();
        log.error("error msg:", ex);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }


    @ExceptionHandler(value = {SQLException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> sqlException(Exception ex) {
        ex.printStackTrace();
        log.error("error msg:", ex);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "db error");
    }


}
