package com.example.taskapi.controller;

import com.example.taskapi.dto.UserResponseDTO;
import com.example.taskapi.errors.Messageerrors;
import com.example.taskapi.service.UserService;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Controller("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    name = "401 Example",
                                    value = """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Authentication is required.",
                      "path": "/api/users"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    name = "403 Example",
                                    value = """
                    {
                      "status": 403,
                      "error": "Forbidden",
                      "message": "You don't have permission to access this resource.",
                      "path": "/api/users"
                    }
                    """
                            )
                    )
            )
    })
    @Get
    public HttpResponse<List<UserResponseDTO>> getUsers() {
        var dto = userService.getAllUsers()
                .stream()
                .map(UserResponseDTO::from)
                .toList();
        return HttpResponse.ok(dto);
    }
}
