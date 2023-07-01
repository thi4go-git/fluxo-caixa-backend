package com.dynss.cloudtecnologia.exception;


import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer statusCode;

    protected BaseException(String errorMessage, int httpStatus) {
        super(errorMessage);
        statusCode = httpStatus;
    }

}
