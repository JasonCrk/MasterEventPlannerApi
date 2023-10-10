package com.LP2.EventScheduler.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(
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
}
