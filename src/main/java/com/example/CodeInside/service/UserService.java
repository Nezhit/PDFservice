package com.example.CodeInside.service;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.User;
import com.example.CodeInside.repos.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserByUsername(String username){
        User user= userRepo.findByUsername(username).orElseThrow(()->{
            return new UsernameNotFoundException("User с именем "+username+"не найден");
        });
        return user;

    }
    public User getUserFromRequest(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username=null;
        if (principal != null) {

            username = principal.getName();
            System.out.println("Username: " + username);
        } else {

            System.out.println("User not authenticated");
        }
        return userRepo.findByUsername(username).get();
    }
//    public Map<String, List<Book>> getAllUsersAndBooks(){
//
//    }
}
