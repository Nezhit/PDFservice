package com.example.CodeInside.service;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.User;
import com.example.CodeInside.pojo.BookshelfCreation;
import com.example.CodeInside.repos.BookRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Value("${upload.dir}")
    private String uploadDir;
    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    // @Async
   public void processBookAsync(MultipartFile file) throws IOException {
        InputStream inputStream=file.getInputStream();
       try (PDDocument document = PDDocument.load(inputStream)) {
                Book book=new Book();


               PDPage page = new PDPage();
               document.addPage(page);

               String fileName = System.currentTimeMillis() + ".pdf";
               Path filePath = Paths.get(uploadDir, fileName);

               try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE)) {
                   byte[] buffer = new byte[1024];
                   int bytesRead;
                   while ((bytesRead = inputStream.read(buffer)) != -1) {
                       outputStream.write(buffer, 0, bytesRead);
                   }
               }



               document.save(filePath.toString());
                book.setFilePath(filePath.toString());
                book.setTitle(file.getOriginalFilename());
                book.setAuthor(document.getDocumentInformation().getAuthor());
                book.setReadingProgress(1);
                bookRepo.save(book);
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           System.out.println("ЗАВЕРШЕНО В СЕРВИСЕ");
       }
   }
   public List<Book> getNoShelfBooks(){
        List<Book>books=bookRepo.findAll();
       List<Book> filteredBooks = books.stream()
               .filter(book -> book.getBookshelf() == null)
               .toList();
        return filteredBooks;
   }
    public List<Book> getBooksWithShell(){
        List<Book>books=bookRepo.findAll();
        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getBookshelf() != null)
                .toList();
        return filteredBooks;
    }
    public List<Book> getAll(){
        return bookRepo.findAll();
    }
    public Book getBookById(Long id){
        Book book= bookRepo.findById(id).orElseThrow(()->{
            return new UsernameNotFoundException("Книги с id "+id+"не найден");
        });
        return book;
    }
    public int getReadingProgress(Long id){
        return bookRepo.findById(id).get().getReadingProgress();
    }
    public void saveProgress(Book book){
        bookRepo.save(book);
    }

}
