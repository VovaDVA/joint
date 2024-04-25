package com.jointProfile.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Properties;

@Service
public class RemoteFileUploader {

    public String uploadAvatarFile(MultipartFile file, String fileName) {
        String serverAddress = "192.168.133.208";
        int serverPort = 22;
        String username = "jointadmin";
        String password = "12345";

        String knownHostsFilePath = "/Users/admin/.ssh/known_hosts/";

        Session session = null;
        Channel channel = null;
        ChannelSftp sftpChannel = null;

        try {
            JSch jsch = new JSch();

            jsch.setKnownHosts(knownHostsFilePath);

            session = jsch.getSession(username,serverAddress,serverPort);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "yes");

            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();

            sftpChannel = (ChannelSftp) channel;

            String remoteDirectoryPath = "/home/jointadmin/images/avatars/";

            InputStream inputStream = file.getInputStream();

            sftpChannel.put(inputStream,remoteDirectoryPath + fileName);

            inputStream.close();

            return "http://192.168.133.208/images/avatars/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file to remote server", e);
        } finally {
            if (sftpChannel != null) {
                sftpChannel.disconnect();
            }

            if (channel != null) {
                channel.disconnect();
            }

            if (session != null) {
                session.disconnect();
            }
        }
    }

    public String uploadBannerFile(MultipartFile file, String fileName) {
        String serverAddress = "192.168.133.208";
        int serverPort = 22;
        String username = "jointadmin";
        String password = "12345";

        String knownHostsFilePath = "/Users/admin/.ssh/known_hosts/";

        Session session = null;
        Channel channel = null;
        ChannelSftp sftpChannel = null;

        try {
            JSch jsch = new JSch();

            jsch.setKnownHosts(knownHostsFilePath);

            session = jsch.getSession(username, serverAddress, serverPort);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "yes");

            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();

            sftpChannel = (ChannelSftp) channel;

            String remoteDirectoryPath = "/home/jointadmin/images/banners/";

            InputStream inputStream = file.getInputStream();

            sftpChannel.put(inputStream,remoteDirectoryPath + fileName);

            inputStream.close();

            return "http://192.168.133.208/images/banners/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file to remote server", e);
        } finally {
            if (sftpChannel != null) {
                sftpChannel.disconnect();
            }

            if (channel != null) {
                channel.disconnect();
            }

            if (session != null) {
                session.disconnect();
            }
        }
    }
}
