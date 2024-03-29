package com.example.CodeInside.controllers;


import com.example.CodeInside.models.Book;
import com.example.CodeInside.models.Mark;
import com.example.CodeInside.pojo.MarkDTO;
import com.example.CodeInside.service.BookService;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Path;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

@Controller
@EnableAsync
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }



    @GetMapping("/upload")
    public String getUploadPage(){
        return "uploadBook";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // Получение InputStream из MultipartFile
            //InputStream inputStream = file.getInputStream();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            bookService.processBookAsync(file,request,authentication);
            System.out.println("ЗАВЕРШЕНО В КОНТРОЛЛЕРЕ");
            return ResponseEntity.ok("Файл успешно загружен");
        } catch (IOException e) {
            e.printStackTrace();
            // Вернуть ответ с ошибкой
            return ResponseEntity.status(500).body("Произошла ошибка при загрузке файла");
        }

    }
    @GetMapping("/pdf/{id}")
    public String pdfPage(@PathVariable int id,Model model){
        model.addAttribute("id",id);
        int currentPage=bookService.getReadingProgress((long) id);
        model.addAttribute("currentPage",currentPage);
        List<Mark> marks=bookService.getMarksByBookId((long)id);
        model.addAttribute("marks",marks);
        return "pdf";
    }
    @GetMapping("/pdf/{bookId}/{pageNumber}")
    public ResponseEntity<?> serveFile(@PathVariable Long bookId,@PathVariable int pageNumber) throws IOException {
        Book book=bookService.getBookById(bookId);

        //File file = new File("src/main/uploads/1701901688860.pdf");
        File file = new File(book.getFilePath());
       // int pageNumber=book.getReadingProgress();
        List<PDDocument> Pages = null;
        try (PDDocument document = PDDocument.load(file)) {
            Splitter splitter = new Splitter();
            Pages = splitter.split(document);
            Iterator<PDDocument> iterator = Pages.listIterator();


            PDDocument pd = new PDDocument();
            if (Pages.size() >= pageNumber) {
                pd = Pages.get(pageNumber - 1);
            } else {
                return ResponseEntity.badRequest().body("Не правильная страница");
            }


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pd.save(byteArrayOutputStream);


            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline").filename("output.pdf").build());
            pd.close();
            book.setReadingProgress(pageNumber);
            bookService.saveProgress(book);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);


        } finally {

            for (PDDocument pageDocument : Pages) {
                pageDocument.close();
            }
        }


    }
    @PostMapping("/appointmark")
    public ResponseEntity<String> appointMark(@RequestBody MarkDTO markDTO){

        return bookService.appointMark(markDTO);
    }
//    @PostMapping("/assertbook")
//    public ResponseEntity<String> assertBook(){
//        return ResponseEntity.ok("OK");
//    }

//    @GetMapping("/pdf/{pageNumber}")
//    public ResponseEntity<?> serveFile(@PathVariable int pageNumber) throws IOException {
//        File file = new File("src/main/uploads/1701901688860.pdf");
//        List<PDDocument> Pages = null;
//        try (PDDocument document = PDDocument.load(file)) {
//            Splitter splitter = new Splitter();
//            Pages = splitter.split(document);
//            Iterator<PDDocument> iterator = Pages.listIterator();
//
//
//            PDDocument pd = new PDDocument();
//            if (Pages.size() >= pageNumber) {
//                pd = Pages.get(pageNumber - 1);
//            } else {
//                return ResponseEntity.badRequest().body("Не правильная страница");
//            }
//
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            pd.save(byteArrayOutputStream);
//
//
//            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDisposition(ContentDisposition.builder("inline").filename("output.pdf").build());
//            pd.close();
//
//            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//
//
//        } finally {
//
//            for (PDDocument pageDocument : Pages) {
//                pageDocument.close();
//            }
//        }
//
//
//    }
//    @GetMapping("/showpdf/{pageNumber}")
//    public ResponseEntity<?> showPdfPage2(@PathVariable int pageNumber, Model model) {
//        byte[] imageData = new byte[0];
//        try {
//            // Загрузка PDF файла по URL
//            File file = new File("D:\\CodeInside\\src\\main\\resources\\static\\KursovoyFinalEnd.pdf");
//
//            // Используем PDFBox для отображения PDF
//            PDDocument document = PDDocument.load(file);
//
//            // Проверяем, что запрашиваемая страница существует
//            int numberOfPages = document.getNumberOfPages();
//            if (pageNumber < 1 || pageNumber > numberOfPages) {
//                throw new IllegalArgumentException("Некорректный номер страницы");
//            }
//
//
//            //Instantiating the PDFRenderer class
//            PDFRenderer renderer = new PDFRenderer(document);
//
//
//            //Rendering an image from the PDF document
//            BufferedImage image = renderer.renderImage(pageNumber, 2F);
//
//
//
//            //Writing the image to a file
//            ImageIO.write(image, "PNG", new File("D:\\CodeInside\\src\\main\\resources\\static\\myimage.png"));
//
//            System.out.println("Image created");
//
//            //Closing the document
//            document.close();
//
//            // Преобразование изображения в формат JPEG и запись в ByteArrayOutputStream
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", baos);
//            baos.flush();
//            imageData = baos.toByteArray();
//
//            // Конвертация байтового массива в строку Base64
//            byte[] imageBytes = baos.toByteArray();
//            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//            baos.close();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ByteArrayResource resource = new ByteArrayResource(imageData);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        headers.setContentLength(imageData.length);
//        // Установите другие заголовки, если необходимо
//
//        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
//    }

}