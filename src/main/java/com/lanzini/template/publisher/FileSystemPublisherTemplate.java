package com.lanzini.template.publisher;

import com.lanzini.exception.FileSystemPublisherException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Useful template to publish messages how to write a json file on FileSystem
 */
public class FileSystemPublisherTemplate {

    /**
     * Write a json file on filesystem
     * @param path path to write the file
     *  example /home/test/file.json
     * @param message message to write
     * @param <T> the type of the message
     * @throws FileSystemPublisherException if something went wrong
     */
    public static <T> void writeFile(String path, T message) throws FileSystemPublisherException {
        writeFile(path,Json.stringify(message));
    }

    /**
     * Write a file on filesystem
     * @param path path to write the file
     * @param message message to write
     * @throws FileSystemPublisherException if something went wrong
     */
    public static void writeFile(String path, String message) throws FileSystemPublisherException {
        try {
            Path path1 = Paths.get(path);
            byte[] strToBytes = message.getBytes();
            Files.write(path1, strToBytes);
        }catch (Exception e){
            throw new FileSystemPublisherException(e.getMessage());
        }
    }

}
