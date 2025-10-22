package com.example.taskapi.errors;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;

@Serdeable
@Schema(name = "ErrorResponse", description = "Standard error body")
public class Messageerrors {

    @Schema(description = "HTTP status code")
    private int status;

    @Schema(description = "HTTP reason")
    private String error;

    @Schema(description = "Human-readable error message")
    private String message;

    @Schema(description = "Request path")
    private String path;

    public Messageerrors() {}

    public Messageerrors(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // getters/setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public static final String USER_CREATED_SUCCESS    = "User created successfully.";
    public static final String USER_CREATION_FAILED    = "User creation failed.";
    public static final String TASK_CREATED_SUCCESS    = "Task created successfully.";
    public static final String TASK_CREATION_FAILED    = "Task creation failed.";
    public static final String USER_NOT_FOUND          = "User not found. Check the id.";
    public static final String INVALID_PASSWORD        = "Password must contain only numbers.";
    public static final String INVALID_USERNAME        = "Username must contain only letters.";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists.";
    public static final String INVALID_USER_ID         = "Invalid user id.";
}
