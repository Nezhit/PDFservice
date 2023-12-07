package com.example.CodeInside.repos;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Bookshelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookshelfRepo extends JpaRepository<Bookshelf,Long> {
}
