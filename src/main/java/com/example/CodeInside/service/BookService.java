package com.example.CodeInside.service;

import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Bookshelf;
import com.example.CodeInside.models.User;
import com.example.CodeInside.repos.BookRepo;
import com.example.CodeInside.repos.BookshelfRepo;
import com.example.CodeInside.repos.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BookService {
    @Value("${upload.dir}")
    private String uploadDir;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;
    private final BookshelfRepo bookshelfRepo;
    private final UserService userService;

    public BookService(BookRepo bookRepo, UserRepo userRepo, BookshelfRepo bookshelfRepo, UserService userService) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.bookshelfRepo = bookshelfRepo;
        this.userService = userService;
    }

    @Async
    public void processBookAsync(MultipartFile file, HttpServletRequest request, Authentication authentication) throws IOException {
        String username = authentication.getName();
        System.out.println("Username: " + username);
        // Создаем временный файл для копирования содержимого
        Path tempFile = Files.createTempFile("temp-file-", ".pdf");
        file.transferTo(tempFile);
        System.out.println("GGGGGGGGG");

        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Bookshelf basicshelf = bookshelfRepo.findByNameAndUserId("Basic Shelf", user.getId()).orElseGet(() -> {
            // Если полки с таким именем не существует, создайте новую
            Bookshelf newShelf = new Bookshelf();
            newShelf.setName("Basic Shelf");
            newShelf.setUser(user);
            return bookshelfRepo.save(newShelf);
        });



        try {


            // Здесь продолжайте работу с временным файлом
            try (PDDocument document = PDDocument.load(tempFile.toFile())) {
                Book book = new Book();

                PDPage page = new PDPage();
                document.addPage(page);

                String fileName = System.currentTimeMillis() + ".pdf";
                Path filePath = Paths.get(uploadDir, fileName);




                document.save(filePath.toString());
                book.setFilePath(filePath.toString());
                book.setTitle(file.getOriginalFilename());
                book.setAuthor(document.getDocumentInformation().getAuthor());
                book.setReadingProgress(1);
                basicshelf.addBook(book);
                book.setBookshelf(basicshelf);
                bookRepo.save(book);
                bookshelfRepo.save(basicshelf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Files.deleteIfExists(tempFile);
            System.out.println("ЗАВЕРШЕНО В СЕРВИСЕ");
            SecurityContextHolder.clearContext();
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
    public List<Book> getAll(HttpServletRequest request){
        User user=userService.getUserFromRequest(request);
        user.getBookshelves().stream().toList();
        //return bookRepo.findAll();
        // return user.getBookshelves().stream().iterator().next().getBooks().stream().toList();
        return bookRepo.findByUserId(user.getId());
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
    public List<Book> getAllBooksFromAllUsers(HttpServletRequest request){
        User user=userService.getUserFromRequest(request);
        return bookRepo.findBooksNotAssociatedWithUser(user.getId());
    }
    @Scheduled(cron = "0 0 */12 * * *") // Периодичность в миллисекундах (здесь 1 минута)
    public void checkAndExecuteEvents() {
        List<Book> books = bookRepo.findAll();
        for (Book book : books) {
            if(book.getDeadline().isBefore(LocalDate.now())){
                bookRepo.delete(book);
            }
        }
    }

}
