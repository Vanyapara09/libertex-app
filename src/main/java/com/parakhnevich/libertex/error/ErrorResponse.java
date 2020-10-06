package com.parakhnevich.libertex.error;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private int status;

}