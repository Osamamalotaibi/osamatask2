package com.example.taskapi.controller;

import com.example.taskapi.aop.Loggable;
import com.example.taskapi.dto.TaskRequestDTO;
import com.example.taskapi.dto.TaskResponseDTO;
import com.example.taskapi.entity.Task;
import com.example.taskapi.entity.TaskType;
import com.example.taskapi.entity.User;
import com.example.taskapi.errors.Messageerrors;
import com.example.taskapi.repository.UserRepository;
import com.example.taskapi.service.TaskService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Controller("/api/tasks")
@Loggable
@Secured(SecurityRule.IS_AUTHENTICATED)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Task", description = "Tasks management endpoints")
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public TaskController(TaskService taskService,
                          UserRepository userRepository,
                          SecurityService securityService) {
        this.taskService = taskService;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    // ---------- Create ----------
    @Post
    @Operation(summary = "Create task for current user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "201 Example",
                                    value = """
                    {"id":3,"title":"osama","completed":false,"type":"MANUAL"}
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    value = """
                    {"status":400,"error":"Bad Request","message":"type must be MANUAL or AUTOMATIC","path":"/api/tasks"}
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    value = """
                    {"status":401,"error":"Unauthorized","message":"Authentication required","path":"/api/tasks"}
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
                                    value = """
                    {"status":403,"error":"Forbidden","message":"Insufficient role: ROLE_USER required","path":"/api/tasks"}
                    """
                            )
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TaskRequestDTO.class),
                    examples = @ExampleObject(
                            name = "TaskRequestDTO Example",
                            value = """
                {"title":"alotaibi","completed":false,"type":"MANUAL"}
                """
                    )
            )
    )
    public HttpResponse<TaskResponseDTO> createTask(@Body @NotNull @Valid TaskRequestDTO requestDTO) {
        String username = securityService.username()
                .orElseThrow(() -> new RuntimeException("Authentication required"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(requestDTO.getTitle());
        task.setCompleted(requestDTO.isCompleted());
        task.setType(TaskType.valueOf(requestDTO.getType().toUpperCase()));
        task.setUser(user);

        Task created = taskService.createTask(task);
        return HttpResponse.status(HttpStatus.CREATED).body(TaskResponseDTO.from(created));
    }

    // ---------- List ----------
    @Get
    @Operation(summary = "List tasks of current user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "Tasks Example",
                                    value = """
                    [
                      {"id":1,"title":"nn","completed":false,"type":"MANUAL"},
                      {"id":2,"title":"aa","completed":true,"type":"AUTOMATIC"}
                    ]
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    value = """
                    {"status":400,"error":"Bad Request","message":"Invalid query parameter","path":"/api/tasks"}
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    value = """
                    {"status":401,"error":"Unauthorized","message":"Authentication required","path":"/api/tasks"}
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
                                    value = """
                    {"status":403,"error":"Forbidden","message":"Insufficient role: ROLE_USER required","path":"/api/tasks"}
                    """
                            )
                    )
            )
    })
    public HttpResponse<List<TaskResponseDTO>> getUserTasks() {
        String username = securityService.username()
                .orElseThrow(() -> new RuntimeException("Authentication required"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<TaskResponseDTO> result = taskService.getTasksByUser(user.getId())
                .stream()
                .map(TaskResponseDTO::from)
                .toList();

        return HttpResponse.ok(result);
    }
}
