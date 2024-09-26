package com.shubham.lightbill.lightbill_backend.custumExceptions;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
