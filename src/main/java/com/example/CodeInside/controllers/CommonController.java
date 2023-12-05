package com.example.CodeInside.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
    @GetMapping("/")
    public String homePage(){
        return "home";
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
