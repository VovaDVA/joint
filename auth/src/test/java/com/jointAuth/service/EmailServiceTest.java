package com.jointAuth.service;

import com.jointAuth.model.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceTest {

    @Mock
    private Session mockSession;

    @Mock
    private Transport mockTransport;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void sendVerificationCodeByEmailSuccess() throws Exception {
        Transport mockTransport = mock(Transport.class);
        Session mockSession = mock(Session.class);

        EmailService emailService = new EmailService();
        emailService.setHost("smtp.gmail.com");
        emailService.setPort(587);
        emailService.setFromEmail("jointtest05@gmail.com");
        emailService.setPassword("iuah wxvg shum fjkq");

        User user = new User();
        user.setEmail("recipient@gmail.com");

        when(mockSession.getTransport("smtp")).thenReturn(mockTransport);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals(emailService.getFromEmail(), ((InternetAddress) args[0]).getAddress());
            assertEquals(user.getEmail(), ((InternetAddress) args[1]).getAddress());
            assertEquals("Разовый код для авторизации", ((MimeMessage) args[2]).getSubject());
            assertNotNull(((MimeMessage) args[2]).getContent());
            return null;
        }).when(mockTransport).sendMessage(any(), any());

        boolean result = emailService.sendVerificationCodeByEmail(user, "123456");

        assertTrue(result);

        assertNotNull(mockSession);
    }

    @Test
    void sendVerificationCodeByEmailInvalidEmail() throws Exception {
        EmailService emailService = new EmailService();
        emailService.host = "smtp.example.com";
        emailService.port = 587;
        emailService.fromEmail = "test@example.com";
        emailService.password = "password123";

        User user = new User();
        user.setEmail("invalid_email");

        Transport mockTransport = mock(Transport.class);
        when(mockSession.getTransport("smtp")).thenReturn(mockTransport);

        emailService.sendVerificationCodeByEmail(user, "123456");

        verify(mockSession, never()).getTransport("smtp");
        verify(mockTransport, never()).sendMessage(any(), any());
    }

    @Test
    void sendVerificationCodeByEmailSendError() throws Exception {
        EmailService emailService = new EmailService();
        emailService.host = "smtp.gmail.com";
        emailService.port = 587;
        emailService.fromEmail = "test@gmail.com";
        emailService.password = "password123";

        User user = new User();
        user.setEmail("recipient@gmail.com");

        doThrow(new MessagingException("Error sending message")).when(mockTransport).sendMessage(any(), any());

        boolean result = emailService.sendVerificationCodeByEmail(user, "123456");

        assertFalse(result);
    }

    @Test
    void sendVerificationCodeByEmailConnectionError() throws Exception {
        EmailService emailService = new EmailService();
        emailService.host = "smtp.gmail.com";
        emailService.port = 587;
        emailService.fromEmail = "test@gmail.com";
        emailService.password = "password123";

        User user = new User();
        user.setEmail("recipient@gmail.com");

        doThrow(new MessagingException("Error connecting to mail server")).when(mockTransport).connect(any(), any());

        boolean result = emailService.sendVerificationCodeByEmail(user, "123456");

        assertFalse(result);
    }
}