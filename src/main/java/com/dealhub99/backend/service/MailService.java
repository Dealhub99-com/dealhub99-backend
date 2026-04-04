package com.dealhub99.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
        String content = "To reset your password, click the link below:\n" + resetLink +
                "\n\nThis link will expire in 15 minutes.";
        sendEmail(to, "Password Reset Request", content);
    }
}
