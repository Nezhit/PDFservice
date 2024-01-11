package com.example.CodeInside.integrated;

import com.example.CodeInside.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.config.name=hibernate-test", "spring.profiles.active=test"})
@Transactional
@Sql(scripts = "classpath:data.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))

public class IntegratedTests {
    @Autowired
    BookService bookService;
    // Replace these values with your actual repository and directory paths
    @Value("${upload.dir}")
    private String uploadDir;
    private static final String USERNAME = "user";


    @Test
    @DirtiesContext
     public void uploadTest() throws IOException {
      Path path = Paths.get("src/main/uploads/1701892908423.pdf");

          MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "fileName",
                "application/pdf",
                Files.readAllBytes(path)
        );
         HttpServletRequest request = null;
        Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, "1234");

        bookService.processBookAsync(multipartFile, request, authentication);


    }

}
