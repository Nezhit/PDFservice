package com.example.CodeInside.controllers;

import com.example.CodeInside.exceptions.UserNotActivatedException;
import com.example.CodeInside.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommonController {
    private final UserService userService;

    public CommonController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage(){
        return "home";
    }
    @GetMapping("/activate/{code}")
    public String activate(Model model,@PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if(!isActivated){
            model.addAttribute("message","Действие ссылки активации закончилось, зарегистрируйтесь заново");
            return "activationerror";
        }
        return "redirect:/login";
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        // Извлекаем информацию об ошибке из атрибутов запроса
        Exception exception = (Exception) request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        if (exception instanceof UserNotActivatedException) {
            model.addAttribute("error", "User not activated. Please check your activation status.");
        } else if (exception instanceof BadCredentialsException) {
            model.addAttribute("error", "Invalid username or password.");
        }

        return "login";
    }
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/activationerror")
    public String activationError(){
        return "activationerror";
    }
}
