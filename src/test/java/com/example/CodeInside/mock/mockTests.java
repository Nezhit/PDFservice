package com.example.CodeInside.mock;

import com.example.CodeInside.controllers.AuthController;
import com.example.CodeInside.models.Role;
import com.example.CodeInside.models.enums.ERole;
import com.example.CodeInside.pojo.CaptchaResponse;
import com.example.CodeInside.pojo.MessageResponse;
import com.example.CodeInside.repos.RoleRepo;
import com.example.CodeInside.repos.UserRepo;
import com.example.CodeInside.service.MailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
public class mockTests {
    @InjectMocks
    private AuthController authController;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private MailService mailService;
    @Test
    public void testRegisterUserSuccess() throws MessagingException {

        MockitoAnnotations.initMocks(this);

        String username = "testUser";
        String email = "test@example.com";
        String password = "testPassword";
        String captchaResponse = "validCaptchaResponse";
        CaptchaResponse captchaResponseObj = new CaptchaResponse();
        captchaResponseObj.setSuccess(true);

        Mockito.when(restTemplate.postForObject(any(String.class), any(), eq(CaptchaResponse.class)))
                .thenReturn(captchaResponseObj);

        // Mock password encoding
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // Mock RoleRepo
        Role roleUser = new Role();
        roleUser.setName(ERole.ROLE_USER);


        Mockito.when(roleRepo.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(roleUser));


        ResponseEntity<?> responseEntity = authController.registerUser(username, email, password, captchaResponse);
        System.out.println(responseEntity.getBody().toString());

        assertEquals(ResponseEntity.ok(new MessageResponse("User CREATED")).getStatusCodeValue(),
                responseEntity.getStatusCodeValue());

        assertEquals("User CREATED", ((MessageResponse) responseEntity.getBody()).getMessage());

    }

    @Test
    public void testRegisterUserInvalidCaptcha() throws MessagingException {

        MockitoAnnotations.initMocks(this);
        String username = "testUser";
        String email = "test@example.com";
        String password = "testPassword";
        String captchaResponse = "invalidCaptchaResponse";
        CaptchaResponse captchaResponseObj = new CaptchaResponse();
        captchaResponseObj.setSuccess(false);

        Mockito.when(restTemplate.postForObject(any(String.class), any(), eq(CaptchaResponse.class)))
                .thenReturn(captchaResponseObj);


        ResponseEntity<?> responseEntity = authController.registerUser(username, email, password, captchaResponse);


        assertEquals(ResponseEntity.badRequest().body("Заполните капчу"), responseEntity);
    }
}
