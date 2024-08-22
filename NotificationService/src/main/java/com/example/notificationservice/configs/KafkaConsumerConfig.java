package com.example.notificationservice.configs;

import com.example.notificationservice.dtos.SendEmailMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import javax.mail.Session;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

@Configuration
@AllArgsConstructor
public class KafkaConsumerConfig {

    private ObjectMapper objectMapper;
    private EmailUtil emailUtil;

    @KafkaListener(topics = "sendEmail", groupId = "notificationService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
//        SendEmailMessageDTO messageDTO = objectMapper.readValue(
//                message,
//                SendEmailMessageDTO.class
//        );
//        System.out.println("Message received: " + message);
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
//        props.put("mail.smtp.port", "587"); //TLS Port
//        props.put("mail.smtp.auth", "true"); //enable authentication
//        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
//
//        //create Authenticator object to pass in Session.getInstance argument
//        Authenticator auth = new Authenticator() {
//            //override the getPasswordAuthentication method
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(messageDTO.getFrom(), "password");
//            }
//        };
//        Session session = Session.getInstance(props, auth);
//
//        EmailUtil.sendEmail(session, messageDTO.getTo(),messageDTO.getSubject(), messageDTO.getBody());
        System.out.println("Mail has been sent" + message);

    }
}
