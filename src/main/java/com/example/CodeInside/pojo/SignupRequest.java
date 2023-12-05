package com.example.CodeInside.pojo;

import java.util.Set;

public class SignupRequest {
    // @NotBlank(message = "Имя пользователя не должно быть пустым")
    // @Pattern(regexp = "\\S+", message = "Имя пользователя не должно состоять только из пробелов")
    private String username;
    private String email;
    private String password;
    private Set<String> roles;

    public SignupRequest() {
    }

    public SignupRequest(String username, String email, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
