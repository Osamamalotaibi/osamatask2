//package com.example.taskapi.security;
//
//import com.example.taskapi.errors.Messageerrors;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class JsonAccessDeniedHandler implements AccessDeniedHandler {
//  private final ObjectMapper om = new ObjectMapper();
//
//  @Override
//  public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) throws IOException {
//    res.setStatus(HttpStatus.FORBIDDEN.value());
//    res.setContentType("application/json");
//    om.writeValue(res.getOutputStream(),
//        new Messageerrors(403, "Forbidden", "You don't have permission to access this resource.", req.getRequestURI()));
//  }
//}
