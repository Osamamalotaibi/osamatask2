package com.example.taskapi.service;

import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

@Singleton
public class ReferenceDataService {

    @Cacheable("referenceData")
    public List<String> getAllowedTaskTypes() {
        return List.of("MANUAL", "AUTOMATIC");
    }

    @Cacheable("referenceData")
    public Boolean getDefaultCompleted() {
        return Boolean.FALSE;
    }

    @Cacheable("referenceData")
    public Map<String, Object> getTaskOptions() {
        return Map.of(
                "types", getAllowedTaskTypes(),
                "defaultCompleted", getDefaultCompleted()
        );
    }
}
