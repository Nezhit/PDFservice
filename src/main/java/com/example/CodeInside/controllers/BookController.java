package com.example.CodeInside.controllers;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@Controller
public class BookController {

    @GetMapping("/showpdf/{pageNumber}")
    public ResponseEntity<?> showPdfPage2(@PathVariable int pageNumber, Model model) {
        byte[] imageData = new byte[0];
        try {
            // Загрузка PDF файла по URL
            File file = new File("D:\\CodeInside\\src\\main\\resources\\static\\KursovoyFinalEnd.pdf");

            // Используем PDFBox для отображения PDF
            PDDocument document = PDDocument.load(file);

            // Проверяем, что запрашиваемая страница существует
            int numberOfPages = document.getNumberOfPages();
            if (pageNumber < 1 || pageNumber > numberOfPages) {
                throw new IllegalArgumentException("Некорректный номер страницы");
            }


            //Instantiating the PDFRenderer class
            PDFRenderer renderer = new PDFRenderer(document);


            //Rendering an image from the PDF document
            BufferedImage image = renderer.renderImage(pageNumber, 2F);



            //Writing the image to a file
            ImageIO.write(image, "PNG", new File("D:\\CodeInside\\src\\main\\resources\\static\\myimage.png"));

            System.out.println("Image created");

            //Closing the document
            document.close();

            // Преобразование изображения в формат JPEG и запись в ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            imageData = baos.toByteArray();

            // Конвертация байтового массива в строку Base64
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            baos.close();


        } catch (IOException e) {
            e.printStackTrace();
            }
        ByteArrayResource resource = new ByteArrayResource(imageData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageData.length);
        // Установите другие заголовки, если необходимо

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}