package com.example.CodeInside.controllers;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Request;
import com.example.CodeInside.pojo.AssertBookRequest;
import com.example.CodeInside.pojo.SetDateRequestDTO;
import com.example.CodeInside.service.BookService;
import com.example.CodeInside.service.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class MainpageController {
    private final BookService bookService;
    private final RequestService requestService;

    public MainpageController(BookService bookService, RequestService requestService) {
        this.bookService = bookService;
        this.requestService = requestService;
    }

    @GetMapping("/main")
    public String getMainPage(Model model, HttpServletRequest request){
        List<Book> books=bookService.getAllBooksFromAllUsers(request);
        model.addAttribute("books",books);
        return "main";
    }
    @PostMapping("/assertbook")
    public ResponseEntity<String> sendRequests(@RequestBody AssertBookRequest assertBookRequest, HttpServletRequest request){

        return requestService.makeRequest(assertBookRequest,request);
    }
    @GetMapping("/requestpage")
    public String getRequestPage(Model model,HttpServletRequest request){
        List<Request> sendedRequests=requestService.getSendedRequests(request);
        List<Request> receivedRequests=requestService.getReceivedRequests(request);
        model.addAttribute("sendedRequests",sendedRequests);
        model.addAttribute("receivedRequests",receivedRequests);
        return "requestpage";
    }
    @PostMapping("/acceptrequest")
    public ResponseEntity<String> acceptRequests(@RequestBody AssertBookRequest assertBookRequest, HttpServletRequest request){

        return requestService.acceptRequest(assertBookRequest);
    }
    @PostMapping("/refuserequest")
    public ResponseEntity<String> refuseRequest(@RequestBody AssertBookRequest assertBookRequest){
        return requestService.refuseRequest(assertBookRequest);
    }
    @PostMapping("/setdate")
    public ResponseEntity<String> setDateBook(@RequestBody SetDateRequestDTO dateRequestDTO){
        return requestService.setDateBook(dateRequestDTO);
    }
}
