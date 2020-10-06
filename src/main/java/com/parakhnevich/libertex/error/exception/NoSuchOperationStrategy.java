package com.parakhnevich.libertex.error.exception;

import com.parakhnevich.libertex.constant.OperationName;

public class NoSuchOperationStrategy extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "There is no strategy for such operation: %s";

    public NoSuchOperationStrategy(OperationName operationName) {
        super(String.format(DEFAULT_MESSAGE, operationName));
    }

}
