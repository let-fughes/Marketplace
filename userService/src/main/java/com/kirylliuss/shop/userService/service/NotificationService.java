package com.kirylliuss.shop.userService.service;

import com.kirylliuss.shop.userService.model.User;
import com.kirylliuss.shop.userService.repository.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Properties;

public class NotificationService {

    @Value("${SANDLER.MAIL.USERNAME}")
    private String sandlerMailUsername;

    @Value("${SANDLER.MAIL.PASSWORD}")
    private String sandlerMailPassword;

    private UserRepository userRepository;

    private Session createSession(){

        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtp.sendpartial", "true");

        return  Session.getDefaultInstance(props);
    }

    private String getUserEmail(String login){
        User user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User Not found."));
        return user.getEmail();
    }

    private Message createMessage(Session session, String subject, String text, String login) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        message.setText(text);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(getUserEmail(login)));
        message.setSentDate(new Date());
        return message;
    }

    public void sendMessage(String subject, String text, String login) throws MessagingException {
        String userLogin = sandlerMailUsername;
        String userPassword = sandlerMailPassword;

        Session session = createSession();
        Message message = createMessage(session, subject, text, login);

        Transport transport = session.getTransport();
        transport.connect("smtp.gmail.com", 465, userLogin, userPassword);

        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
    }
}
