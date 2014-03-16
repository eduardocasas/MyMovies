package com.eduardocasas.app.service;

import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
public class FileService {

    public static void uploadFile(MultipartFile file, String folder_path, String file_name) {
        try {
            byte[] bytes = file.getBytes();
            new File(folder_path).mkdirs();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(folder_path+"/"+file_name)));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void convertImageToJpg(String file_path) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(file_path));
            BufferedImage newBufferedImage = new BufferedImage(
                bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB
            );
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            ImageIO.write(newBufferedImage, "jpg", new File(file_path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void resizeImage(int width, int height, String source_file, String destiny_file) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(source_file));
            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizedImage = new BufferedImage(width, height, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(resizedImage, "jpg", new File(destiny_file)); 
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void removeFile(String file_path) {
        new File(file_path).delete();
    }
    
    public static void removeFolder(String folder_path) {
        try {
            FileUtils.deleteDirectory(new File(folder_path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
