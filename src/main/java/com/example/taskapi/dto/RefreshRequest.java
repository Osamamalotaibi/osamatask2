package com.example.taskapi.dto;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Schema(name = "RefreshRequest", description = "Request body to refresh an access token")
public class RefreshRequest {

    @NotBlank
    @Schema(description = "Valid refresh JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh...")
    private String refreshToken;

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
