package com.example.CodeInside.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

    public UserNotActivatedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserNotActivatedException(String msg) {
        super(msg);
    }
}