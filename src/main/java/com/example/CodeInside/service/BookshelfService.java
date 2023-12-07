package com.example.CodeInside.service;

import com.example.CodeInside.models.Bookshelf;
import com.example.CodeInside.models.User;
import com.example.CodeInside.pojo.BookshelfCreation;
import com.example.CodeInside.repos.BookshelfRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookshelfService {
    private final BookshelfRepo bookshelfRepo;

    public BookshelfService(BookshelfRepo bookshelfRepo) {
        this.bookshelfRepo = bookshelfRepo;
    }

    public ResponseEntity<String> createShelf(BookshelfCreation bookshelfCreation, User user){
        Bookshelf bookshelf=new Bookshelf(user, bookshelfCreation.getName());
        bookshelfRepo.save(bookshelf);
        return ResponseEntity.ok("Полка создана");
    }
    public List<Bookshelf> getAll(){
        return bookshelfRepo.findAll();
    }
}
