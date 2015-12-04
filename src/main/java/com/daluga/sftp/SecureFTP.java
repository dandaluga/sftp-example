package com.daluga.sftp;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.util.Properties;

public class SecureFTP {

    private static final String HOST = "XXX.XXX.XXX.XXX";
    private static final int PORT = 22;
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String KNOWN_HOSTS = "/location/of/known/host/file/.ssh/known_hosts";

    public static void main(String[] args) {
        System.out.println("Starting secure ftp session...");

        // TODO: Refactor all this into easier to maintain code.

        // Create a new instance if the Jcraft implementation of the ssh protocol
        JSch jsch = new JSch();

        // Set a logger so that we can get additional diagnostics from the library
        JSch.setLogger(new SecureFTPLogger());

        Session session = null;
        Channel channel = null;

        try {
            session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);
            session.setTimeout(15000); // In milliseconds

            // OPTION 1: Do not verify the public key (not secure).
            // Setting the StrictHostKeyChecking to no will not verify the public key of the ssh /sftp server.
            // As a result, this method of connecting is less secure (man in the middle attack).
//            Properties config = new Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);

            // OPTION 2: Connect with the public host key from the server that is in the known hosts file.
            // Follow these steps:
            // 1. Get the public key server: cat /etc/ssh/ssh_host_rsa_key.pub
            // 2. Add the public key to your known_hosts file
            jsch.setKnownHosts(KNOWN_HOSTS);

            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.get("index_default.html", "localfile.txt");

            channelSftp.put(new FileInputStream(new File("localfile.txt")), "remotefile.txt");

            channelSftp.exit();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
