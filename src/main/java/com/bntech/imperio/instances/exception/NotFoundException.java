package com.bntech.imperio.instances.exception;

/***
 * Exception that should be thrown when site can't be found
 * @message Message of exception
 */
public class NotFoundException extends Exception {
    public NotFoundException(final String message) {
        super(message);
    }
}
