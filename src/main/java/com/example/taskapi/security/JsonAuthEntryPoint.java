//package com.example.taskapi.security;
//
//import com.example.taskapi.errors.Messageerrors;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class JsonAuthEntryPoint implements AuthenticationEntryPoint {
//  private final ObjectMapper om = new ObjectMapper();
//
//  @Override
//  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
//    res.setStatus(HttpStatus.UNAUTHORIZED.value());
//    res.setContentType("application/json");
//    om.writeValue(res.getOutputStream(),
//        new Messageerrors(401, "Unauthorized", "Authentication is required.", req.getRequestURI()));
//  }
//}
