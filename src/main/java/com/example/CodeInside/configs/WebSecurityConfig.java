package com.example.CodeInside.configs;

import com.example.CodeInside.service.UserDetailsServiceImpl;
import com.example.CodeInside.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;


    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http



                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home","/register","/activate/*","/login","/activationerror").permitAll()
                        .requestMatchers("/api/auth/signin").permitAll()
                        .requestMatchers("/api/auth/signup").permitAll()
                        .requestMatchers("/moder").hasRole("MODERATOR")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Указываем URL страницы входа
                        .permitAll() // Разрешаем всем доступ к странице входа
                        .failureHandler(customAuthenticationFailureHandler())


                )





                .logout((logout) -> logout.permitAll())
                .authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



}