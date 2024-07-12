package com.example.BeFETest.Error;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String errorMessage = String.format("Unauthorized - Please log in. Request URL: %s", request.getRequestURI());
        response.getWriter().write(String.format("{\"status\": 401, \"message\": \"%s\", \"exception\": \"%s\"}", errorMessage, authException.getMessage()));


        //response.setContentType("application/json;charset=UTF-8");
        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 에러가 발생한 함수 이름을 포함
        //String functionName = authException.getStackTrace()[0].getMethodName();
        //String errorMessage = String.format("Unauthorized - Please log in. Request URL: %s, Function: %s", request.getRequestURI(), functionName);

        //response.getWriter().write(String.format("{\"status\": 401, \"message\": \"%s\", \"exception\": \"%s\"}", errorMessage, authException.getMessage()));


    }
}


