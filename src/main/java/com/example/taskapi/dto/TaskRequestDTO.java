package com.example.taskapi.dto;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Serdeable
@Schema(name = "TaskRequestDTO", description = "Payload for creating or updating a task")
public class TaskRequestDTO {

    @Schema(example = "osama", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Title must not be blank")
    private String title;

    @Schema(example = "false", description = "Whether the task is completed")
    private boolean completed;

    @Schema(
            example = "MANUAL",
            description = "Allowed values: MANUAL | AUTOMATIC",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Type must not be blank")
    @Pattern(regexp = "MANUAL|AUTOMATIC", message = "Type must be MANUAL or AUTOMATIC")
    private String type;

    public TaskRequestDTO() {}

    public TaskRequestDTO(String title, boolean completed, String type) {
        this.title = title;
        this.completed = completed;
        this.type = type;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
