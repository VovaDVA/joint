package com.jointAuth.service;

import com.jointAuth.model.user.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService {

    @Value("${email.from}")
    private String fromEmail;

    @Value("${email.password}")
    private String password;

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private int port;

    public void sendVerificationCodeByEmail(User user, String verificationCode) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));

        jakarta.mail.Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            String emailSubject = createEmailSubject();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(emailSubject);

            String emailContent = createEmailContent(user, verificationCode);
            message.setText(emailContent);

            Transport.send(message);
        } catch (MessagingException e) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "Error sending verification code email to " + user.getEmail(), e);
        }
    }

    private String createEmailSubject() {
        return "Разовый код";
    }

    private String createEmailContent(User user, String verificationCode) {
        return String.format("Здравствуйте, %s %s!\n\n" +
                        "Мы получили запрос на отправку разового кода для вашей учетной записи Joint.\n\n" +
                        "Ваш разовый код: %s\n\n" +
                        "Если вы не запрашивали этот код, никому не сообщайте его и обратитесь в службу технической поддержки учетных записей Joint.\n\n" +
                        "С уважением,\n" +
                        "команда Joint",
                user.getFirstName(), user.getLastName(), verificationCode);
    }
}
