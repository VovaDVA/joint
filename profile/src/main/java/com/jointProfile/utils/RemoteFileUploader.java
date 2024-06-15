package com.jointProfile.utils;

import com.jcraft.jsch.JSch; // библиотека для подключения к серверу по SSH
import com.jcraft.jsch.*;
import java.io.File;
import java.io.InputStream;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/* класс необходимый  для загрузки файла на удаленный сервер через SFTP и возвращения URL для доступа к этому файлу
и сохранения этого URL в БД */
@Service
public class RemoteFileUploader {

    private final JSch jsch;
    private final String host;
    private final int port;
    private final String username;
    private final String password;


    public RemoteFileUploader(JSch jsch,
                              @Value("${ssh.host}") String host,
                              @Value("${ssh.port}") int port,
                              @Value("${ssh.username}") String username,
                              @Value("${ssh.password}") String password) {
        this.jsch = jsch;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String uploadFileOnServer(MultipartFile file, String fileNameOnServer, String nameOfFolder) {
        String knownHostsPath = "/home/jointadmin/.ssh/known_hosts";
        //String knownHostsPath = "C:\\Users\\ASUS//.ssh/known_hosts";

        Session session = null;
        ChannelSftp channelSftp = null;
        InputStream inputStream = null;

        try {
            // Установка файла known_hosts
            File knownHostsFile = new File(knownHostsPath);
            jsch.setKnownHosts(knownHostsFile.getAbsolutePath());

            session = jsch.getSession(username, host, port);
            session.setPassword(password);

            // Строгая проверка ключа хоста
            session.setConfig("StrictHostKeyChecking", "yes");

            session.connect();

            // Открытие канала SFTP - для передачи файлов между клиентом и сервером
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            String remoteDirectory = "/home/jointadmin/images/" + nameOfFolder + "/";

            // Передача файла на удаленный сервер
            inputStream = file.getInputStream(); // получает поток данных из загруженного файла и сохраняет его в переменную inputStream

            channelSftp.cd(remoteDirectory);
            channelSftp.put(inputStream, fileNameOnServer);

            inputStream.close();

            // Возвращаем URL
            return "http://" + host + "/images/" + nameOfFolder + "/" + fileNameOnServer;

        } catch (JSchException | SftpException | java.io.IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось загрузить файл на удаленный сервер", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }


    public void deleteFileFromServer(String fileNameOnServer, String nameOfFolder) {
        String knownHostsPath = "/home/jointadmin/.ssh/known_hosts";

        Session session = null;
        ChannelSftp channelSftp = null;

        try {

            File knownHostsFile = new File(knownHostsPath);

            jsch.setKnownHosts(knownHostsFile.getAbsolutePath());

            session = jsch.getSession(username, host, port);
            session.setPassword(password);

            session.setConfig("StrictHostKeyChecking", "yes");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            String newFileName = fileNameOnServer.substring(fileNameOnServer.lastIndexOf("/") + 1);

            String remoteDirectory = "/home/jointadmin/images/" + nameOfFolder + "/";

            channelSftp.rm(remoteDirectory + newFileName);

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось удалить файл на удаленном сервере", e);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }

        }
    }
}
