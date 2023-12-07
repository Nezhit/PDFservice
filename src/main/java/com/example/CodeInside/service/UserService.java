package com.example.CodeInside.service;

import com.example.CodeInside.models.User;
import com.example.CodeInside.repos.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
