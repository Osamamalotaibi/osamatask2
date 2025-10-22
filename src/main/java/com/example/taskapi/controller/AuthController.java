
package com.example.taskapi.controller;

import com.example.taskapi.aop.Loggable;
import com.example.taskapi.dto.*;
import com.example.taskapi.entity.User;
import com.example.taskapi.errors.Messageerrors;
import com.example.taskapi.repository.UserRepository;
import com.example.taskapi.security.jwt.JwtUtil;
import com.example.taskapi.security.PasswordService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller("/auth")
@Loggable

@Tag(name = "Auth", description = "Authentication endpoints (login, register, refresh)")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthController(JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordService passwordService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    // === LOGIN ===
    @Post(uri = "/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Login and get tokens")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "200 Example",
                                    value = """
                {
                  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.access...",
                  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh..."
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    name = "401 Example",
                                    value = """
                {
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "Invalid username or password.",
                  "path": "/auth/login"
                }
                """
                            )
                    )
            )
    })

    public HttpResponse<?> login(@Body AuthRequest request) {
        LOG.info("Login request received for username: {}", request.getUsername());
        try {
            Optional<User> maybeUser = userRepository.findByUsername(request.getUsername());
            if (maybeUser.isEmpty()) {
                return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                        .body(new Messageerrors(401, "Unauthorized",
                                "Invalid username or password.", "/auth/login"));
            }

            User user = maybeUser.get();
            boolean ok = passwordService.matches(request.getPassword(), user.getPassword());
            if (!ok) {
                return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                        .body(new Messageerrors(401, "Unauthorized",
                                "Invalid username or password.", "/auth/login"));
            }

            String accessToken  = jwtUtil.generateToken(user.getUsername());
            String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

            return HttpResponse.ok(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            LOG.error("Error during login for user: {}", request.getUsername(), e);
            return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(new Messageerrors(401, "Unauthorized",
                            "Invalid username or password.", "/auth/login"));
        }
    }
    @Post(uri = "/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Register new user", description = "Creates a user with ROLE_USER.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleMessage.class),
                            examples = @ExampleObject(
                                    name = "201 Example",
                                    value = """
                { "status": 201, "message": "User registered successfully" }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Username exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleMessage.class),
                            examples = @ExampleObject(
                                    name = "400 Example",
                                    value = """
                { "status": 400, "message": "Username already exists." }
                """
                            )
                    )
            )
    })
    public HttpResponse<SimpleMessage> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    name = "RegisterRequest Example",
                                    value = """
                { "username": "osama", "password": "1" }
                """
                            )
                    )
            )
            @Valid @Body RegisterRequest request
    ) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return HttpResponse.badRequest(new SimpleMessage(400, "Username already exists."));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordService.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return HttpResponse.created(new SimpleMessage(201, "User registered successfully"))
                .headers(h -> h.location(URI.create("/api/users?username=" + user.getUsername())));
    }


    @Post(uri = "/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Refresh access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New access token returned",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "200 Example",
                                    value = """
                {
                  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.access...",
                  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh..."
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Refresh token invalid or expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Messageerrors.class),
                            examples = @ExampleObject(
                                    name = "401 Example",
                                    value = """
                {
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "Refresh token is expired or missing",
                  "path": "/auth/refresh"
                }
                """
                            )
                    )
            )
    })
    public HttpResponse<?> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshRequest.class),
                            examples = @ExampleObject(
                                    name = "RefreshRequest Example",
                                    value = """
                    { "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh..." }
                    """
                            )
                    )
            )
            @Body RefreshRequest request
    ) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || jwtUtil.isTokenExpired(refreshToken)) {
            return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(new Messageerrors(401, "Unauthorized",
                            "Refresh token is expired or missing", "/auth/refresh"));
        }
        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateToken(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", refreshToken);
        return HttpResponse.ok(tokens);
    }}
