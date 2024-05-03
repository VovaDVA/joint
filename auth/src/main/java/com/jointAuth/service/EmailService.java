package com.jointAuth.service;

import com.jointAuth.model.user.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Data
public class EmailService {

    @Value("${email.from}")
    protected String fromEmail;

    @Value("${email.password}")
    protected String password;

    @Value("${email.host}")
    protected String host;

    @Value("${email.port}")
    protected int port;

    public boolean sendVerificationCodeByEmail(User user, String verificationCode) {
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
            String emailSubject = createEmailSubjectFor2FA();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(emailSubject);

            String emailContent = createEmailContentFor2FA(user, verificationCode);
            message.setText(emailContent);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "Ошибка при отправке проверочного кода по электронной почте на " + user.getEmail(), e);
            return false;
        }
    }

    private String createEmailSubjectFor2FA() {
        return "Разовый код для авторизации";
    }

    private String createEmailContentFor2FA(User user, String verificationCode) {
        return String.format("Здравствуйте, %s %s!\n\n" +
                        "Мы получили запрос на отправку разового кода для вашей учетной записи Joint.\n\n" +
                        "Ваш разовый код: %s\n\n" +
                        "Если вы не запрашивали этот код, никому не сообщайте его и обратитесь в службу технической поддержки учетных записей Joint.\n\n" +
                        "С уважением,\n" +
                        "команда Joint",
                user.getFirstName(), user.getLastName(), verificationCode);
    }

    public boolean sendPasswordChangeConfirmationEmail(User user, String confirmationCode) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            String emailSubject = createEmailSubjectForChangePassword();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(emailSubject);

            String emailContent = createEmailContentForChangePassword(user, confirmationCode);
            message.setText(emailContent);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, "Ошибка при отправке проверочного кода по электронной почте на " + user.getEmail(), e);
            return false;
        }
    }

    private String createEmailSubjectForChangePassword() {
        return "Разовый код для обновления пароля";
    }


    private String createEmailContentForChangePassword(User user, String confirmationCode) {
        return String.format(
                "Здравствуйте, %s %s!\n\n" +
                        "Мы получили запрос на изменение пароля для вашей учетной записи Joint.\n\n" +
                        "Ваш код подтверждения для изменения пароля: %s\n\n" +
                        "Если вы не запрашивали изменение пароля, никому не сообщайте этот код и обратитесь в службу поддержки учетных записей Joint.\n\n" +
                        "С уважением,\n" +
                        "команда Joint",
                user.getFirstName(),
                user.getLastName(),
                confirmationCode
        );
    }

    public boolean sendAccountDeletionConfirmationEmail(User user, String verificationCode) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            String emailSubject = createEmailSubjectForAccountDeletion();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(emailSubject);

            String emailContent = createEmailContentForAccountDeletion(user, verificationCode);
            message.setText(emailContent);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, "Ошибка при отправке проверочного кода по электронной почте на " + user.getEmail(), e);
            return false;
        }
    }

    private String createEmailSubjectForAccountDeletion() {
        return "Код подтверждения для удаления учетной записи";
    }

    private String createEmailContentForAccountDeletion(User user, String verificationCode) {
        return String.format(
                "Здравствуйте, %s %s!\n\n" +
                        "Мы получили запрос на удаление вашей учетной записи Joint.\n\n" +
                        "Ваш код подтверждения для удаления учетной записи: %s\n\n" +
                        "Если вы не запрашивали удаление учетной записи, никому не сообщайте этот код и обратитесь в службу поддержки учетных записей Joint.\n\n" +
                        "С уважением,\n" +
                        "команда Joint",
                user.getFirstName(),
                user.getLastName(),
                verificationCode
        );
    }

    public boolean sendPasswordResetConfirmationEmail(User user, String resetCode) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            String emailSubject = createEmailSubjectForPasswordReset();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(emailSubject);

            String emailContent = createEmailContentForPasswordReset(user, resetCode);
            message.setText(emailContent);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, "Ошибка при отправке проверочного кода по электронной почте на " + user.getEmail(), e);
            return false;
        }
    }

    private String createEmailSubjectForPasswordReset() {
        return "Код для сброса пароля";
    }

    private String createEmailContentForPasswordReset(User user, String resetCode) {
        return String.format(
                "Здравствуйте, %s %s!\n\n" +
                        "Мы получили запрос на сброс пароля для вашей учетной записи Joint.\n\n" +
                        "Ваш код подтверждения для сброса пароля: %s\n\n" +
                        "Если вы не запрашивали сброс пароля, никому не сообщайте этот код и обратитесь в службу поддержки учетных записей Joint.\n\n" +
                        "С уважением,\n" +
                        "Команда Joint",
                user.getFirstName(),
                user.getLastName(),
                resetCode
        );
    }
}
