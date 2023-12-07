package com.example.CodeInside.controllers;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Bookshelf;
import com.example.CodeInside.models.User;
import com.example.CodeInside.pojo.BookshelfCreation;
import com.example.CodeInside.service.BookService;
import com.example.CodeInside.service.BookshelfService;
import com.example.CodeInside.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class LibraryController {
    private final BookService bookService;
    private final UserService userService;
    private final BookshelfService bookshelfService;
    public LibraryController(BookService bookService, UserService userService, BookshelfService bookshelfService) {
        this.bookService = bookService;
        this.userService = userService;
        this.bookshelfService = bookshelfService;
    }

    @GetMapping("/library")
    public String getLibrary(Model model){
//        List<Book> booksNoShelf=bookService.getNoShelfBooks();
//        model.addAttribute("booksNoShelf",booksNoShelf);
//        List<Book> booksWithShelf=bookService.getBooksWithShell();
//        model.addAttribute("booksWithShelf",booksWithShelf);
        List<Book>books=bookService.getAll();
        model.addAttribute("books",books);
        List<Bookshelf> shelves=bookshelfService.getAll();
        model.addAttribute("shelves",shelves);
        return "library";
    }
    @GetMapping("/createshelf")
    public String getCreateShelf(){
        return "shelfcreation";
    }
    @PostMapping("/createshelf")
    public ResponseEntity<String> createShelt(@RequestBody BookshelfCreation bookshelfCreation){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       User user= userService.getUserByUsername(authentication.getName());


        return bookshelfService.createShelf(bookshelfCreation,user);
    }
}
