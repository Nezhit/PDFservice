package com.example.CodeInside.models;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookshelf_id")
    private Bookshelf bookshelf;

    private String title;
    private String author;

    @Column(name = "reading_progress")
    private int readingProgress; // Прогресс чтения (номер страницы)

    public Book() {
    }

    public Book(Bookshelf bookshelf, String title, String author, int readingProgress) {
        this.bookshelf = bookshelf;
        this.title = title;
        this.author = author;
        this.readingProgress = readingProgress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getReadingProgress() {
        return readingProgress;
    }

    public void setReadingProgress(int readingProgress) {
        this.readingProgress = readingProgress;
    }
}
