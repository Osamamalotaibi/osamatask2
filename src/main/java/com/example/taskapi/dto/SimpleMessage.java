package com.example.taskapi.dto;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;

@Serdeable
@Schema(name = "SimpleMessage", description = "Generic response wrapper with status and message")
public class SimpleMessage {

    @Schema(example = "200", description = "HTTP status code")
    private int status;

    @Schema(example = "User registered successfully", description = "Human-readable message")
    private String message;

    public SimpleMessage() {}

    public SimpleMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
