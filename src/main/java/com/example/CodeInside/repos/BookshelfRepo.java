package com.example.CodeInside.repos;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Bookshelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookshelfRepo extends JpaRepository<Bookshelf,Long> {
    Optional<Bookshelf> findByNameAndUserId(String name, Long id);
    List<Bookshelf> findByUserId(Long id);
}
