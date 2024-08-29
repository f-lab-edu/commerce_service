package com.wming.ecservice.common.exception;

public class FailedPaymentException extends RuntimeException{
    public FailedPaymentException(String message) {
        super(message);
    }
}
