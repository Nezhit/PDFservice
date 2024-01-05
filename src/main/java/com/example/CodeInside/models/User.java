package com.example.CodeInside.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames="username"),
        @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles=new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Bookshelf> bookshelves = new HashSet<>();
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Request> requests = new HashSet<>();
    private String activationCode;
    private LocalDateTime date;
    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    @PostPersist
    public void onUserCreate() {

        Bookshelf basicShelf = new Bookshelf();
        basicShelf.setName("Basic Shelf");
        basicShelf.setUser(this);
        bookshelves.add(basicShelf);
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<Bookshelf> getBookshelves() {
        return bookshelves;
    }

    public void setBookshelves(Set<Bookshelf> bookshelves) {
        this.bookshelves = bookshelves;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
