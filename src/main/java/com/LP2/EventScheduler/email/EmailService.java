package com.LP2.EventScheduler.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private void sendMail(
            String to,
            EmailContent emailContent,
            Map<String, String> data
    ) throws MessagingException {
        MimeMessage emailMessage = this.emailSender.createMimeMessage();

        emailMessage.setFrom(this.fromEmail);
        emailMessage.setRecipients(MimeMessage.RecipientType.TO, to);
        emailMessage.setSubject(emailContent.getSubject());
        emailMessage.setContent(emailContent.getBody(data), "text/html; charset=utf-8");

        this.emailSender.send(emailMessage);
    }

    @Async
    public void sendEmail(
            String to,
            EmailContent emailContent,
            Map<String, String> data
    ) {
        try {
            this.sendMail(to, emailContent, data);
        } catch (MessagingException e) {
            log.error("Ocurrió un error al tratar de enviar el email al correo " + to);
        }

        CompletableFuture.completedFuture(null);
    }
}
