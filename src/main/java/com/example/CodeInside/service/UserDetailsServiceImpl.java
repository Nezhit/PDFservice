package com.example.CodeInside.service;
import com.example.CodeInside.exceptions.UserNotActivatedException;
import com.example.CodeInside.models.User;
import com.example.CodeInside.repos.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepo.findByUsername(username).orElseThrow(()->{
            return new UsernameNotFoundException("User с именем "+username+"не найден");
        });
        if(user.getActivationCode()!=null){
             throw new UserNotActivatedException("User not activated: " + username);
        }
        return UserDetailsImpl.build(user);
    }
}