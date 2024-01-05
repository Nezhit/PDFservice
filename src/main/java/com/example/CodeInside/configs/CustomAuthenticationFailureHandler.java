package com.example.CodeInside.configs;

import com.example.CodeInside.exceptions.UserNotActivatedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String message = "Invalid username or password.";
        if (exception instanceof UserNotActivatedException) {
            message = "Your account is not activated yet. Please check your email for activation instructions.";
        }
        request.getSession().setAttribute("error", message);
        response.sendRedirect("/activationerror");
    }
}