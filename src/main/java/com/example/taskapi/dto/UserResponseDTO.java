package com.example.taskapi.dto;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import com.example.taskapi.entity.User;

@Serdeable
@Schema(name = "UserResponseDTO", description = "Response object representing a user")
public class UserResponseDTO {

    @Schema(description = "User id", example = "1")
    private Long id;

    @Schema(description = "User role", example = "ROLE_USER")
    private String role;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public static UserResponseDTO from(User u) {
        return new UserResponseDTO(u.getId(), u.getRole());
    }
}
