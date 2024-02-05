package com.example.certificateGenerator.errorhandling;

import org.springframework.core.NestedRuntimeException;

public class PathNotFoundException extends NestedRuntimeException {


    public PathNotFoundException(String msg) {
        super(msg);
    }
}
