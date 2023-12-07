package com.example.CodeInside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CodeInsideApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeInsideApplication.class, args);
	}

}
