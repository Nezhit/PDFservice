package com.example.CodeInside.repos;

import com.example.CodeInside.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book,Long> {
    Optional<Book> findByAuthor(String author);
    @Query("SELECT b FROM Book b WHERE b.bookshelf.user.id = :userId")
    List<Book> findByUserId(@Param("userId") Long userId);
    @Query("SELECT b FROM Book b WHERE b.bookshelf.user.id != :userId OR b.bookshelf IS NULL")
    List<Book> findBooksNotAssociatedWithUser(@Param("userId") Long userId);
}
