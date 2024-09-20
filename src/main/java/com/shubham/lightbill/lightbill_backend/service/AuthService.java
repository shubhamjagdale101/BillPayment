package com.shubham.lightbill.lightbill_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String email, String username, String otp) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("otp", otp);
        String htmlContent = templateEngine.process("otp-email-template.html", context);

        helper.setFrom("shubhamjagdalerxl@gmail.com");
        helper.setTo(email);
        helper.setSubject("Your OTP Code");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
