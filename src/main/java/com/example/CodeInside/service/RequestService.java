package com.example.CodeInside.service;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Bookshelf;
import com.example.CodeInside.models.Request;
import com.example.CodeInside.models.User;
import com.example.CodeInside.models.enums.EStatus;
import com.example.CodeInside.pojo.AssertBookRequest;
import com.example.CodeInside.pojo.SetDateRequestDTO;
import com.example.CodeInside.repos.BookRepo;
import com.example.CodeInside.repos.BookshelfRepo;
import com.example.CodeInside.repos.RequestRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    private final UserService userService;
    private final BookService bookService;
    private final RequestRepo requestRepo;
    private final BookRepo bookRepo;
    private final BookshelfRepo bookshelfRepo;

    public RequestService(UserService userService, BookService bookService, RequestRepo requestRepo, BookRepo bookRepo, BookshelfRepo bookshelfRepo) {
        this.userService = userService;
        this.bookService = bookService;
        this.requestRepo = requestRepo;
        this.bookRepo = bookRepo;
        this.bookshelfRepo = bookshelfRepo;
    }

    public ResponseEntity<String> makeRequest(AssertBookRequest assertBookRequest, HttpServletRequest request){
        Request req=new Request();
        User user=userService.getUserFromRequest(request);
        req.setSender(user);
        Book book = bookService.getBookById(assertBookRequest.getId());
        req.setBook(book);
        req.setText(assertBookRequest.getText());

        req.setRecipient(book.getBookshelf().getUser());
        req.setStatus(EStatus.AWAIT);
        requestRepo.save(req);
        return ResponseEntity.ok("Все отлично");


    }
    public List<Request> getSendedRequests(HttpServletRequest request){
        User user=userService.getUserFromRequest(request);
        return requestRepo.findBySender(user);
    }
    public List<Request> getReceivedRequests(HttpServletRequest request){
        User user=userService.getUserFromRequest(request);
        return requestRepo.findByRecipient(user);
    }
    public ResponseEntity<String> acceptRequest(AssertBookRequest assertBookRequest){
        Request request=requestRepo.findById(assertBookRequest.getId()).get();
        Optional<Bookshelf> basicShelfOptional = request.getSender()
                .getBookshelves()
                .stream()
                .filter(bookshelf -> "Basic Shelf".equals(bookshelf.getName()))
                .findFirst();
        Book book=new Book(basicShelfOptional.get(),request.getBook().getTitle(),request.getBook().getAuthor(),request.getBook().getFilePath(),1);
        basicShelfOptional.get().addBook(book);
        bookRepo.save(book);
        bookshelfRepo.save(basicShelfOptional.get());
        request.setStatus(EStatus.ACCEPTED);
        requestRepo.save(request);
        return ResponseEntity.ok("Успешно");

    }
    public ResponseEntity<String> refuseRequest(AssertBookRequest assertBookRequest){
        Request request=requestRepo.findById(assertBookRequest.getId()).get();
        request.setStatus(EStatus.CANCELLED);
        request.setText(assertBookRequest.getText());
        requestRepo.save(request);
        return ResponseEntity.ok("Отменено");
    }
    public ResponseEntity<String> setDateBook(SetDateRequestDTO dateRequestDTO){
        Request request=requestRepo.findById(dateRequestDTO.getId()).get();
        Optional<Bookshelf> basicShelfOptional = request.getSender()
                .getBookshelves()
                .stream()
                .filter(bookshelf -> "Basic Shelf".equals(bookshelf.getName()))
                .findFirst();
        Book book=new Book(basicShelfOptional.get(),request.getBook().getTitle(),request.getBook().getAuthor(),request.getBook().getFilePath(),1);
        book.setDeadline(dateRequestDTO.getDate());
        basicShelfOptional.get().addBook(book);
        bookRepo.save(book);
        bookshelfRepo.save(basicShelfOptional.get());
        request.setStatus(EStatus.ACCEPTED);
        requestRepo.save(request);
        return ResponseEntity.ok("Успешно");
    }
}
