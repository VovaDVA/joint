package com.jointProfile.utils;

import com.jcraft.jsch.*;
import com.jointProfile.config.JSchConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {JSchConfig.class})
public class RemoteFileUploaderTest {

    @Mock
    private JSch jsch;

    @Mock
    private Session session;

    @Mock
    private ChannelSftp channelSftp;

    @Mock
    private MultipartFile file;

    @Value("${ssh.host}")
    private String host;

    @Value("${ssh.port}")
    private int port;

    @Value("${ssh.username}")
    private String username;

    @Value("${ssh.password}")
    private String password;

    private RemoteFileUploader remoteFileUploader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        remoteFileUploader = new RemoteFileUploader(jsch, host, port, username, password);
    }

    @Test
    public void testUploadFileOnServer_Success() throws Exception {

        // Мокаем ожидаемое поведение JSch, Session и ChannelSftp
        when(jsch.getSession(username, host, port)).thenReturn(session);
        doNothing().when(session).setPassword(password);
        doNothing().when(session).setConfig(anyString(), anyString());
        doNothing().when(session).connect();
        when(session.openChannel("sftp")).thenReturn(channelSftp);
        doNothing().when(channelSftp).connect();

        // Мокаем поведение файла
        InputStream mockInputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(mockInputStream);

        // Мокаем поведение загрузки через SFTP
        doNothing().when(channelSftp).cd(anyString());
        doNothing().when(channelSftp).put(any(InputStream.class), anyString());

        // Вызываем тестируемый метод
        String result = remoteFileUploader.uploadFileOnServer(file, "testFileName", "testFolder");

        // Проверяем, что метод был вызван с ожидаемыми параметрами
        verify(jsch).getSession(username, host, port);
        verify(session).setPassword(password);
        verify(session).setConfig("StrictHostKeyChecking", "yes");
        verify(session).connect();
        verify(session).openChannel("sftp");
        verify(channelSftp).connect();
        verify(channelSftp).cd("/home/jointadmin/images/testFolder/");
        verify(channelSftp).put(mockInputStream, "testFileName");

        // Проверяем результат
        assertEquals("http://" + host + "/images/testFolder/testFileName", result);
    }

    @Test
    public void testUploadFileOnServer_JSchException() throws Exception {
        when(jsch.getSession(username, "invalidHost", 23))
                .thenThrow(new JSchException("Connection refused"));

        assertThrows(RuntimeException.class,
                () -> remoteFileUploader.uploadFileOnServer(file, "testFileName", "testFolder"));

        verify(session, never()).setPassword(password);
        verify(session, never()).setConfig(anyString(), anyString());
        verify(session, never()).connect();
        verify(session, never()).openChannel("sftp");
        verify(channelSftp, never()).connect();
        verify(channelSftp, never()).cd(anyString());
        verify(channelSftp, never()).put(any(InputStream.class), anyString());
    }

    @Test
    public void testUploadFileOnServer_SFTPFailure() throws Exception {
        // Мокаем ожидаемое поведение JSch, Session и ChannelSftp
        when(jsch.getSession(username, host, port)).thenReturn(session);
        doNothing().when(session).setPassword(password);
        doNothing().when(session).setConfig(anyString(), anyString());
        doNothing().when(session).connect();
        when(session.openChannel("sftp")).thenReturn(channelSftp);
        doNothing().when(channelSftp).connect();

        // Мокаем поведение файла
        InputStream mockInputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(mockInputStream);

        // Ошибка при поведении загрузки через SFTP
        doThrow(new SftpException(1, "Не удалось загрузить файл"))
                .when(channelSftp).cd(anyString());

        assertThrows(RuntimeException.class, () ->
                remoteFileUploader.uploadFileOnServer(file, "testFileName", "testFolder"));

        verify(channelSftp, never()).put(any(InputStream.class), anyString());
    }

    @Test
    public void testUploadFileOnServer_IOException() throws Exception {
        when(jsch.getSession(username, host, port)).thenReturn(session);
        doNothing().when(session).setPassword(password);
        doNothing().when(session).setConfig(anyString(), anyString());
        doNothing().when(session).connect();
        when(session.openChannel("sftp")).thenReturn(channelSftp);
        doNothing().when(channelSftp).connect();
        InputStream mockInputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(mockInputStream);
        doNothing().when(channelSftp).cd(anyString());

        doThrow(new IOException("Ошибка при закрытии input stream")).when(mockInputStream).close();

        assertThrows(RuntimeException.class, () -> {
            remoteFileUploader.uploadFileOnServer(file, "testFileName", "testFolder");
        });

        verify(jsch).getSession(username, host, port);
        verify(session).setPassword(password);
        verify(session).setConfig("StrictHostKeyChecking", "yes");
        verify(session).connect();
        verify(session).openChannel("sftp");
        verify(channelSftp).connect();
        verify(channelSftp).cd("/home/jointadmin/images/testFolder/");
        verify(channelSftp).put(mockInputStream, "testFileName");
    }

    @Test
    public void testDeleteFileOnServer_Success() throws Exception {

        // Мокаем ожидаемое поведение JSch, Session и ChannelSftp
        when(jsch.getSession(username, host, port)).thenReturn(session);
        doNothing().when(session).setPassword(password);
        doNothing().when(session).setConfig(anyString(), anyString());
        doNothing().when(session).connect();
        when(session.openChannel("sftp")).thenReturn(channelSftp);
        doNothing().when(channelSftp).connect();

        // Мокаем поведение файла
        InputStream mockInputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(mockInputStream);

        // Мокаем поведение удаления
        doNothing().when(channelSftp).rm(anyString());

        // Вызываем тестируемый метод
        remoteFileUploader.deleteFileFromServer("testFileName", "testFolder");

        // Проверяем, что метод был вызван с ожидаемыми параметрами
        verify(jsch).getSession(username, host, port);
        verify(session).setPassword(password);
        verify(session).setConfig("StrictHostKeyChecking", "yes");
        verify(session).connect();
        verify(session).openChannel("sftp");
        verify(channelSftp).connect();
        verify(channelSftp).rm("/home/jointadmin/images/testFolder/testFileName");

    }

    @Test
    public void testDeleteFileOnServer_JSchException() throws Exception {
        when(jsch.getSession(username, "invalidHost", 23))
                .thenThrow(new JSchException("Connection refused"));

        assertThrows(RuntimeException.class,
                () -> remoteFileUploader.deleteFileFromServer( "testFileName", "testFolder"));

        verify(session, never()).setPassword(password);
        verify(session, never()).setConfig(anyString(), anyString());
        verify(session, never()).connect();
        verify(session, never()).openChannel("sftp");
        verify(channelSftp, never()).connect();
        verify(channelSftp, never()).rm(anyString());


    }

    @Test
    public void testDeleteFileOnServer_SFTPFailure() throws Exception {

        when(jsch.getSession(username, host, port)).thenReturn(session);
        doNothing().when(session).setPassword(password);
        doNothing().when(session).setConfig(anyString(), anyString());
        doNothing().when(session).connect();
        when(session.openChannel("sftp")).thenReturn(channelSftp);
        doNothing().when(channelSftp).connect();

        // Мокаем поведение файла
        InputStream mockInputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(mockInputStream);

        // Ошибка при поведении загрузки через SFTP
        doThrow(new SftpException(1, "Не удалось удалить файл"))
                .when(channelSftp).rm(anyString());

        assertThrows(RuntimeException.class, () ->
                remoteFileUploader.deleteFileFromServer("testFileName", "testFolder"));

    }

}