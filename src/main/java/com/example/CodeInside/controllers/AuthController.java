package com.example.CodeInside.controllers;

import com.example.CodeInside.models.Role;
import com.example.CodeInside.models.User;
import com.example.CodeInside.pojo.CaptchaResponse;
import com.example.CodeInside.service.MailService;
import com.example.CodeInside.service.UserDetailsImpl;
import com.example.CodeInside.models.enums.ERole;
import com.example.CodeInside.pojo.LoginRequest;
import com.example.CodeInside.pojo.MessageResponse;
import com.example.CodeInside.pojo.SignupRequest;
import com.example.CodeInside.repos.RoleRepo;
import com.example.CodeInside.repos.UserRepo;
import com.example.CodeInside.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    private final static String CAPTCHA_URL="https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Value("${recaptcha.secret}")
    private String secret;
    @Autowired
    private MailService mailService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo userRespository;
    @Autowired
    UserService userService;

    @Autowired
    RoleRepo roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/sign")
    public String authUser(@RequestBody LoginRequest loginRequest) {
        User user=userService.getUserByUsername(loginRequest.getUsername());
        if (user.getActivationCode() != null ) {
            return "redirect:/hello";
        }
        System.out.println("AASSASASASAS");
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);



        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println(userDetails.getEmail()+userDetails.getPassword()+userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        System.out.println("Все хорошо");

        return "redirect:/hello";
    }
    @Transactional
    @PostMapping("/signup")
    public synchronized ResponseEntity<?> registerUser(@RequestParam("username") String username,
                                                       @RequestParam("email") String email,
                                                       @RequestParam("password") String password,
                                                       @RequestParam("g-recaptcha-response") String captchaResponse) throws MessagingException {
        String url=String.format(CAPTCHA_URL,secret,captchaResponse);
        CaptchaResponse response= restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponse.class);
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body("Заполните капчу");
        }

        if (userRespository.existsByUsername(username)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }

        if (userRespository.existsByEmail(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is exist"));
        }

        User user = new User(username,
                email,
                passwordEncoder.encode(password));
        user.setActivationCode(UUID.randomUUID().toString());

       // Set<String> reqRoles = signupRequest.getRoles();
        Set<String> reqRoles =new HashSet<>();
        reqRoles.add("");
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "admin":
                        Role adminRole = roleRepository
                                .findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository
                                .findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error, Role MODERATOR is not found"));
                        roles.add(modRole);

                        break;

                    default:
                        Role userRole = roleRepository
                                .findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setDate(LocalDateTime.now());
        userRespository.save(user);
        if(!StringUtils.isEmpty(user.getEmail())){
            String message=String.format("Hello, %s "+"check link: http://localhost:8080/activate/%s",user.getUsername(),user.getActivationCode());
            mailService.send(user.getEmail(),"Activation",message);
        }
        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }

//    @Transactional
//    @PostMapping("/signup")
//    public synchronized ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest,@RequestParam("g-recaptcha-response")String captchaResponse) {
//        String url=String.format(CAPTCHA_URL,secret,captchaResponse);
//        CaptchaResponse response= restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponse.class);
//        if(!response.isSuccess()){
//            return ResponseEntity.badRequest().body("Заполните капчу");
//        }
//
//        if (userRespository.existsByUsername(signupRequest.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is exist"));
//        }
//
//        if (userRespository.existsByEmail(signupRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is exist"));
//        }
//
//        User user = new User(signupRequest.getUsername(),
//                signupRequest.getEmail(),
//                passwordEncoder.encode(signupRequest.getPassword()));
//
//        Set<String> reqRoles = signupRequest.getRoles();
//        Set<Role> roles = new HashSet<>();
//
//        if (reqRoles == null) {
//            Role userRole = roleRepository
//                    .findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
//            roles.add(userRole);
//        } else {
//            reqRoles.forEach(r -> {
//                switch (r) {
//                    case "admin":
//                        Role adminRole = roleRepository
//                                .findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
//                        roles.add(adminRole);
//
//                        break;
//                    case "mod":
//                        Role modRole = roleRepository
//                                .findByName(ERole.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RuntimeException("Error, Role MODERATOR is not found"));
//                        roles.add(modRole);
//
//                        break;
//
//                    default:
//                        Role userRole = roleRepository
//                                .findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
//                        roles.add(userRole);
//                }
//            });
//        }
//        user.setRoles(roles);
//        userRespository.save(user);
//        return ResponseEntity.ok(new MessageResponse("User CREATED"));
//    }
}
