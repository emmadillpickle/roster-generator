package com.emmaong.rostermanager.backend.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, long id) {
        super(resource + " not found: " + id);
    }
}
