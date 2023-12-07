package com.example.CodeInside.repos;

import com.example.CodeInside.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book,Long> {
    Optional<Book> findByAuthor(String author);
}
