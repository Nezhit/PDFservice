package com.example.CodeInside.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }
    public void send(String emailTo,String subject,String message) throws MessagingException {

        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(message);

            emailSender.send(mimeMessage);



        } catch (MessagingException | MailException e) {
            throw new MessagingException("Ошибка при отправке письма.", e);

        }
    }
}
