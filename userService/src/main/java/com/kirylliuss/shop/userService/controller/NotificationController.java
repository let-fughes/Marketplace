package com.kirylliuss.shop.userService.controller;

import com.kirylliuss.shop.userService.service.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(Principal principal, String subject, String text) {
        try{
            String login = principal.getName();
            notificationService.sendMessage(subject, text, login);
            return ResponseEntity.ok("Message sent successfully.");
        } catch (MessagingException ex){
            ex.getStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while sending a message.");
    }


}
