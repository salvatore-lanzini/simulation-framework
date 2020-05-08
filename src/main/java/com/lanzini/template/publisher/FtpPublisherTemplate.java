package com.lanzini.template.publisher;

import com.lanzini.exception.FtpPublisherTemplateException;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Useful template to publish a message how to ftp uploader
 */
public class FtpPublisherTemplate {

    /**
     * Create a json file and upload to Ftp server
     * @param server ftp server host
     * @param port ftp server port
     * @param user ftp server username
     * @param password ftp server password
     * @param message json file to create and send
     * @param pathFileSource path to create file
     * @param pathServerTarget server path to upload file
     * @param <T> type of the message
     * @throws FtpPublisherTemplateException if something went wrong
     */
    public static <T> void createFileAndSend(String server, int port, String user, String password,
            T message, String pathFileSource, String pathServerTarget) throws FtpPublisherTemplateException{
        try{
            createFileAndSend(server,port,user,password,Json.stringify(message),pathFileSource,pathServerTarget);
        }catch(Exception e){
            throw new FtpPublisherTemplateException(e.getMessage());
        }
    }

    /**
     * Create file and upload to Ftp server
     * @param server ftp server host
     * @param port ftp server port
     * @param user ftp server username
     * @param password ftp server password
     * @param message file to create and send
     * @param pathFileSource path to create file
     * @param pathServerTarget server path to upload file
     * @throws FtpPublisherTemplateException if something went wrong
     */
    public static void createFileAndSend(String server, int port, String user, String password,
                            String message, String pathFileSource, String pathServerTarget) throws FtpPublisherTemplateException{
        try{
            FileSystemPublisherTemplate.writeFile(pathFileSource,message);
            send(server,port,user,password,pathFileSource,pathServerTarget);
        }catch(Exception e){
            throw new FtpPublisherTemplateException(e.getMessage());
        }
    }

    /**
     * upload file to Ftp server
     * @param server ftp server host
     * @param port ftp server port
     * @param user ftp server username
     * @param password ftp server password
     * @param pathFileSource path to file
     * @param pathServerTarget server path to upload file
     * @throws FtpPublisherTemplateException if something went wrong
     */
    public static void send(String server, int port, String user, String password,
                            String pathFileSource, String pathServerTarget) throws FtpPublisherTemplateException{
        try{
            FTPClient ftpClient = openConnection(server,port,user,password);
            File file = new File(pathFileSource);
            ftpClient.storeFile(pathServerTarget, new FileInputStream(file));
            ftpClient.disconnect();
        }catch(Exception e){
            throw new FtpPublisherTemplateException(e.getMessage());
        }
    }

    private static FTPClient openConnection(String server, int port, String user, String password) throws FtpPublisherTemplateException {
        try {
            FTPClient ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            ftp.connect(server, port);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }
            ftp.login(user, password);
            return ftp;
        }catch (Exception e){
            throw new FtpPublisherTemplateException(e.getMessage());
        }
    }
}
