package com.example.taskapi.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TaskResponseDTO(Long id, String title, boolean completed, String type) {
    public static TaskResponseDTO from(com.example.taskapi.entity.Task t) {
        return new TaskResponseDTO(t.getId(), t.getTitle(), t.isCompleted(), t.getType().name());
    }
}
