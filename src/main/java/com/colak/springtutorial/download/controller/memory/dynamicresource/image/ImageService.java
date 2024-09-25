package com.colak.springtutorial.download.controller.memory.dynamicresource.image;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ImageService {

    // Base directory where images are stored
    private static final String BASE_DIR = "src/main/resources/static/images/";

    public byte[] addWatermark(String filePath) throws IOException {

        // Combine the base directory with the provided file name
        File imageFile = new File(BASE_DIR + filePath);

        BufferedImage watermarkImage = getWatermarkedImage(imageFile);

        // Extract the file extension from the filePath
        String fileExtension = getFileExtension(filePath);

        // Convert BufferedImage to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(watermarkImage, fileExtension, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private BufferedImage getWatermarkedImage(File imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        BufferedImage watermarkImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());

        Graphics2D g = (Graphics2D) watermarkImage.getGraphics();

        // Copy original image
        g.drawImage(originalImage, 0, 0, null);

        // Set watermark properties
        g.setColor(new Color(255, 0, 0, 128)); // Red color with transparency
        g.setFont(new Font("Arial", Font.BOLD, 20));

        String watermark = "Watermark";
        FontMetrics fontMetrics = g.getFontMetrics();

        // Calculate the position for the watermark in the bottom right corner
        int x = originalImage.getWidth() - fontMetrics.stringWidth(watermark) - 10; // 10 pixels from the right edge
        int y = originalImage.getHeight() - 10; // 10 pixels from the bottom edge

        g.drawString(watermark, x, y);
        g.dispose();
        return watermarkImage;
    }

    // Helper method to get the file extension
    private String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            return filePath.substring(dotIndex + 1).toLowerCase(); // Return the extension in lowercase
        }
        return "jpg"; // Default to "jpg" if no extension is found
    }
}

