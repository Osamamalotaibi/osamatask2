package com.example.taskapi.dto;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Schema(name = "RegisterRequest", description = "Payload for registering a new user")
public class RegisterRequest {

    @NotBlank(message = "must not be blank")
    @Schema(example = "osama", description = "Username of the new account")
    private String username;

    @NotBlank(message = "must not be blank")
    @Schema(example = "123456", description = "Password of the new account")
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters/setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
