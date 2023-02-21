package com.bntech.imperio.instances.exception;

import org.springframework.http.HttpStatus;

public interface ThrowableTransformService {
    String getMessage();
    HttpStatus getHttpStatus();
}
