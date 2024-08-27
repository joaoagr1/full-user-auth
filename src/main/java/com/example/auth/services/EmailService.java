package com.example.auth.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        String subject = "Confirm your email address";
        String confirmationUrl = "http://localhost:8080/api/auth/confirm?token=" + token;
        String message = "<html><body><p>Please confirm your email address by clicking the following link:</p>"
                + "<a href='" + confirmationUrl + "'>Confirm Email</a></body></html>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);  // Set to true for HTML content

        mailSender.send(mimeMessage);
    }
}
