//package com.example.taskapi.security.service;
//
//import com.example.taskapi.repository.UserRepository;
//import io.micronaut.security.authentication.AuthenticationFailed;
//import io.micronaut.security.authentication.AuthenticationRequest;
//import io.micronaut.security.authentication.AuthenticationResponse;
//import io.micronaut.security.authentication.AuthenticationProviderUserPassword;
//import jakarta.inject.Singleton;
//import reactor.core.publisher.Mono;
//
//@Singleton
//public class CustomAuthenticationProvider implements AuthenticationProviderUserPassword {
//
//    private final UserRepository userRepository;
//
//    public CustomAuthenticationProvider(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest<String, String> request) {
//        String username = request.getIdentity();
//        String password = request.getSecret();
//
//        return Mono.fromSupplier(() ->
//                userRepository.findByUsername(username)
//                        .filter(user -> user.getPassword().equals(password)) // استبدلها بالـ BCrypt
//                        .map(user -> AuthenticationResponse.success(user.getUsername()))
//                        .orElse(AuthenticationResponse.failure(AuthenticationFailed.CREDENTIALS_DO_NOT_MATCH))
//        );
//    }
//}
