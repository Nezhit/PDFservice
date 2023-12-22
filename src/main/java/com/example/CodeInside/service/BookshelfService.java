package com.example.CodeInside.service;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Bookshelf;
import com.example.CodeInside.models.User;
import com.example.CodeInside.pojo.AssertBookandShelf;
import com.example.CodeInside.pojo.BookshelfCreation;
import com.example.CodeInside.repos.BookRepo;
import com.example.CodeInside.repos.BookshelfRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookshelfService {
    private final BookshelfRepo bookshelfRepo;
    private final BookRepo bookRepo;
    private final UserService userService;

    public BookshelfService(BookshelfRepo bookshelfRepo, BookRepo bookRepo, UserService userService) {
        this.bookshelfRepo = bookshelfRepo;
        this.bookRepo = bookRepo;
        this.userService = userService;
    }

    public ResponseEntity<String> createShelf(BookshelfCreation bookshelfCreation, User user){
        Bookshelf bookshelf=new Bookshelf(user, bookshelfCreation.getName());
        bookshelfRepo.save(bookshelf);
        return ResponseEntity.ok("Полка создана");
    }
    public List<Bookshelf> getAll(HttpServletRequest request){
        User user=userService.getUserFromRequest(request);
        //return bookshelfRepo.findAll();
       // return user.getBookshelves().stream().toList();
        return bookshelfRepo.findByUserId(user.getId());
    }
    public ResponseEntity<String> assertBookandShelf(AssertBookandShelf assertBookandShelf){
        Bookshelf bookshelf= bookshelfRepo.findById(assertBookandShelf.getShelfId()).orElseThrow(()->{
            return new UsernameNotFoundException("Полки с id "+assertBookandShelf.getShelfId()+"не найден");
        });
        Book book=bookRepo.findById(assertBookandShelf.getBookId()).orElseThrow(()->{
            return new UsernameNotFoundException("Книги с id "+assertBookandShelf.getShelfId()+"не найден");
        });
        bookshelf.addBook(book);
        book.setBookshelf(bookshelf);
        bookshelfRepo.save(bookshelf);
        bookRepo.save(book);
        return ResponseEntity.ok("Книга положена на полку");
    }
}
