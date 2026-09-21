package com.example.pr7.exception;

public class AccountNotZeroException extends RuntimeException {

    public AccountNotZeroException(String message) {
        super(message);
    }
}