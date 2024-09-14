package com.authentication.module.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        String subject = "Confirme seu endereço de e-mail";
        String confirmationUrl = "http://192.168.0.24:8585/auth/confirm?token=" + token;
        String message = "<html><body><p>Por favor, confirme seu endereço de e-mail clicando no link abaixo:</p>"
                + "<a href='" + confirmationUrl + "'>Confirmar E-mail</a></body></html>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);

        mailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        String subject = "Redefinição de Senha";

        Context context = new Context();
        context.setVariable("token", token);
        String message = templateEngine.process("emails/password-reset", context);

        sendHtmlEmail(to, subject, message);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // HTML content

        mailSender.send(mimeMessage);
    }
}
