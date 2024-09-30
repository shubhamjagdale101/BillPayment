package com.shubham.lightbill.lightbill_backend.custumExceptions;

public class JwtAuthenticationException extends Exception{
    public JwtAuthenticationException(String msg){
        super(msg);
    }
}
