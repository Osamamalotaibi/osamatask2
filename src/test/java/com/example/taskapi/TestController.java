package com.example.taskapi;

import com.example.taskapi.dto.TaskRequestDTO;
import com.example.taskapi.entity.Task;
import com.example.taskapi.entity.User;
import com.example.taskapi.repository.UserRepository;
import com.example.taskapi.service.TaskService;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.SecurityService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@MicronautTest
class TaskControllerTest {

    @Inject @Client("/")
    HttpClient client;

    @Inject TaskService taskService;
    @Inject UserRepository userRepository;

    @io.micronaut.test.annotation.MockBean(TaskService.class)
    TaskService taskServiceMock() {
        return Mockito.mock(TaskService.class);
    }

    @io.micronaut.test.annotation.MockBean(UserRepository.class)
    UserRepository userRepositoryMock() {
        return Mockito.mock(UserRepository.class);
    }


    @Singleton
    @Replaces(SecurityService.class)
    static class FakeSecurityService implements SecurityService {
        @Override
        public Optional<String> username() {
            return Optional.of("testuser");
        }
        @Override
        public boolean isAuthenticated() {
            return true;
        }

        @Override
        public boolean hasRole(String role) {
            return false;
        }

        @Override
        public Optional<Authentication> getAuthentication() {
            Map<String, Object> attrs = Collections.emptyMap();
            return Optional.of(Authentication.build("testuser", attrs));
        }
    }

    @Test
    void testCreateTask_ok_200() {
        when(userRepository.findByUsername(eq("testuser")))
                .thenReturn(Optional.of(new User()));

        Task saved = new Task();
        saved.setTitle("cbcbcbb");
        saved.setCompleted(false);
        when(taskService.createTask(any(Task.class))).thenReturn(saved);

        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("xxxx");
        dto.setCompleted(false);
        dto.setType("manuale");

        HttpRequest<TaskRequestDTO> req = HttpRequest.POST("/api/tasks", dto)
                .contentType(MediaType.APPLICATION_JSON);

        HttpResponse<Task> resp = client.toBlocking().exchange(req, Task.class);

        assertEquals(HttpStatus.OK, resp.getStatus());
    }
}
