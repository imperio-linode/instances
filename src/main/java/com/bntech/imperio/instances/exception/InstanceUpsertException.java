package com.bntech.imperio.instances.exception;

public class InstanceUpsertException extends Exception {
    public InstanceUpsertException(String err) {
        super("Incorrect request body provided. Can't add instances. " + err);
    }
}
