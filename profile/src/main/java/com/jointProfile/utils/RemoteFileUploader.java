package com.jointProfile.utils;

import com.jcraft.jsch.JSch; // библиотека для подключения к серверу по SSH
import com.jcraft.jsch.*;
import java.io.File;
import java.io.InputStream;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/* класс необходимый  для загрузки файла на удаленный сервер через SFTP и возвращения URL для доступа к этому файлу
и сохранения этого URL в БД */
@Service
public class RemoteFileUploader {

    public String uploadFileOnServer(MultipartFile file, String fileNameOnServer, String nameOfFolder) {

        String host = "192.168.68.124";
        int port = 22; // порт для подключения по ssh
        String username = "jointadmin";
        String password = "12345";

        //String knownHostsPath = System.getProperty("user.home") + "/.ssh/known_hosts";

        String knownHostsPath = "C:\\Users\\ASUS/.ssh/known_hosts";

        Session session = null;
        ChannelSftp channelSftp = null;
        InputStream inputStream = null;

        try {
            JSch jsch = new JSch();

            // Установка файла known_hosts
            File knownHostsFile = new File(knownHostsPath);
            jsch.setKnownHosts(knownHostsFile.getAbsolutePath());

            session = jsch.getSession(username, host, port);
            session.setPassword(password);

            // Строгая проверка ключа хоста
            session.setConfig("StrictHostKeyChecking", "yes");

            session.connect();

            System.out.println("Подключение установлено!");

            // Открытие канала SFTP - для передачи файлов между клиентом и сервером
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            String remoteDirectory = "/home/jointadmin/images/" + nameOfFolder + "/";

            // Передача файла на удаленный сервер
            inputStream = file.getInputStream(); // получает поток данных из загруженного файла и сохраняет его в переменную inputStream

            channelSftp.cd(remoteDirectory); // переход в указанную директорию
            channelSftp.put(inputStream, fileNameOnServer); // перенос файла

            inputStream.close();

            // Возвращаем URL
            String fileUrl = "http://" + host + "/images/" + nameOfFolder + "/" + fileNameOnServer;
            System.out.println(fileUrl);

            return fileUrl;

        } catch (JSchException | SftpException | java.io.IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file to remote server", e);
        } finally {
            // Закрываем соединения
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
}
