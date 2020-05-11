package com.lanzini.template.publisher;

import com.lanzini.template.connect.PublisherTemplateConnectionFactory;
import com.lanzini.enums.ConnectionTypeEnum;
import com.lanzini.exception.FileSystemPublisherException;
import com.lanzini.exception.FtpPublisherException;
import com.lanzini.exception.PublisherTemplateConnectionException;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;

/**
 * Useful template to publish a message how to ftp uploader
 */
public class FtpPublisherTemplate {

    /**
     * Create a json file and upload to Ftp server
     *
     * @param message          json file to create and send
     * @param pathFileSource   path to create file
     * @param pathServerTarget server path to upload file
     * @param <T>              type of the message
     * @throws FtpPublisherException if something went wrong
     */
    public static <T> void createFileAndSend(T message, String pathFileSource,
                                             String pathServerTarget) throws FtpPublisherException {
        createFileAndSend(Json.stringify(message), pathFileSource, pathServerTarget);
    }

    /**
     * Create file and upload to Ftp server
     *
     * @param message          file to create and send
     * @param pathFileSource   path to create file
     * @param pathServerTarget server path to upload file
     * @throws FtpPublisherException if something went wrong
     */
    public static void createFileAndSend(String message, String pathFileSource,
                                         String pathServerTarget) throws FtpPublisherException {
        try {
            FileSystemPublisherTemplate.writeFile(pathFileSource, message);
            send(pathFileSource, pathServerTarget);
        } catch (FileSystemPublisherException e) {
            throw new FtpPublisherException(e.getMessage());
        }
    }

    /**
     * upload file to Ftp server
     *
     * @param pathFileSource   path to file
     * @param pathServerTarget server path to upload file
     * @throws FtpPublisherException if something went wrong
     */
    public static void send(String pathFileSource, String pathServerTarget) throws FtpPublisherException {
        FTPClient ftpClient = openConnection();
        try {
            File file = new File(pathFileSource);
            ftpClient.storeFile(pathServerTarget, new FileInputStream(file));
        } catch (Exception e) {
            throw new FtpPublisherException(e.getMessage());
        }
    }

    private static FTPClient openConnection() {
        if (PublisherTemplateConnectionFactory.getFtpClient() == null)
            throw new PublisherTemplateConnectionException(ConnectionTypeEnum.FTP);
        else return PublisherTemplateConnectionFactory.getFtpClient();
    }
}
