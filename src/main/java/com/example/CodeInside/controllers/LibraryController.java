package com.example.CodeInside.controllers;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Bookshelf;
import com.example.CodeInside.models.User;
import com.example.CodeInside.pojo.AssertBookandShelf;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<Book> allBooks = bookService.getAll();
        model.addAttribute("allBooks", allBooks);

        List<Bookshelf> shelves = bookshelfService.getAll();
        model.addAttribute("shelves", shelves);

        // Map to store bookshelf IDs for each book
        Map<Long, Long> bookshelfMap = new HashMap<>();
        for (Book book : allBooks) {
            if (book.getBookshelf() != null) {
                bookshelfMap.put(book.getId(), book.getBookshelf().getId());
            }
        }
        model.addAttribute("bookshelfMap", bookshelfMap);

        // Filter out books that are already on shelves
        List<Book> booksNotOnShelves = allBooks.stream()
                .filter(book -> book.getBookshelf() == null)
                .collect(Collectors.toList());
        model.addAttribute("booksNotOnShelves", booksNotOnShelves);

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
    @PostMapping("/assert")
    public ResponseEntity<String > assertBookwithShelves(@RequestBody AssertBookandShelf assertBookandShelf){

        return bookshelfService.assertBookandShelf(assertBookandShelf);

    }

}
