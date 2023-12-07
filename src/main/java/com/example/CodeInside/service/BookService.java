package com.example.CodeInside.service;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.repos.BookRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
@Service
public class BookService {
    @Value("${upload.dir}")
    private String uploadDir;
    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    // @Async
   public void processBookAsync(InputStream inputStream) {
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

               System.out.println(filePath);

               document.save(filePath.toString());
                book.setFilePath(filePath.toString());
                book.setTitle(document.getDocumentInformation().getTitle());
           System.out.println("TITLE= "+document.getDocumentInformation().getTitle());
                book.setAuthor(document.getDocumentInformation().getAuthor());
                book.setReadingProgress(1);
                bookRepo.save(book);
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           System.out.println("ЗАВЕРШЕНО В СЕРВИСЕ");
       }
   }

}
