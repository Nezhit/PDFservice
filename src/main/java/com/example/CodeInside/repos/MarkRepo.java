package com.example.CodeInside.repos;

import com.example.CodeInside.models.Bookshelf;
import com.example.CodeInside.models.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepo extends JpaRepository<Mark,Long> {
    List<Mark> findByBookId(Long bookId);
}
