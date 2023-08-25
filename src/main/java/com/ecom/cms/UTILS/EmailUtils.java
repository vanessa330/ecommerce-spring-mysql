package com.ecom.cms.UTILS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender emailSender;

    public void sendNotification(String to, String subject, String text, List<String> list) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("devtest33010@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if (list != null && list.size() > 0) {
            message.setCc(getCcArray(list));
        }
        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList) {
        String[] cc = new String[ccList.size()];
        for (int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    public void sendResetPassword(String to, String subject, String oneTimeToken) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("devtest33010@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlMsg = "<p> <b>Click below link for reset your password.</b> </br>" +
                "</br> <a href=\"http://localhost:4200/resetPassword/" + oneTimeToken +
                "\"> Click here to reset your password. </a> </p>";

        message.setContent(htmlMsg, "text/html");
        emailSender.send(message);
    }
}
