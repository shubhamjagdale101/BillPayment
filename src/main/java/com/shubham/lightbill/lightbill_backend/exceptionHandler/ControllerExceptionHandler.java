package com.shubham.lightbill.lightbill_backend.exceptionHandler;

import com.shubham.lightbill.lightbill_backend.annotation.RateLimitAspect;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse<Object> responseException(Exception e){
        logger.warn(e.getClass().getName());
        logger.warn(Arrays.toString(e.getStackTrace()));
        return ApiResponse.error(e.getMessage(),HttpStatus.BAD_REQUEST.value());
    }
}
