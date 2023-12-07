package com.example.CodeInside.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookshelves")
public class Bookshelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    @OneToMany(mappedBy = "bookshelf", cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    public Bookshelf() {
    }

    public Bookshelf(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Bookshelf(User user, Set<Book> books) {
        this.user = user;
        this.books = books;
    }
    public Bookshelf(User user, Set<Book> books,String name) {
        this.user = user;
        this.books = books;
        this.name=name;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}

