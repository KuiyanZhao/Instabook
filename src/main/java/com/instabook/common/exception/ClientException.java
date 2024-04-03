package com.instabook.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * format the exception message
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClientException extends RuntimeException {

    protected int code = 400;

    public ClientException(String message) {
        super(message);
    }

    public ClientException(ClientErrorEnum clientErrorEnum) {
        super(clientErrorEnum.getMessage());
    }

    public ClientException(ClientErrorEnum clientErrorEnum, String message) {
        super(message);
        this.code = clientErrorEnum.getCode();
    }

}
