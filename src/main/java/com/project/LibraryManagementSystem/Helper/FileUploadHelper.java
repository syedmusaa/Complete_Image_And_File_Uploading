package com.project.LibraryManagementSystem.Helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.FileSystemResource;

@Component
public class FileUploadHelper {

    private final String UPLOAD_DIR;

    public FileUploadHelper() {
        // Hard-coded path to save files
        this.UPLOAD_DIR = "C:\\Users\\dell\\OneDrive\\Desktop\\LibraryManagementSystem\\src\\main\\resources\\static\\uploads";

        // Create the directory if it doesn't exist
        File uploadDir = new File(this.UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    // Method to upload the file
    public boolean uploadFile(MultipartFile multipartFile) {
        boolean isUploaded = false;

        try {
            if (multipartFile.isEmpty()) {
                throw new IOException("The uploaded file is empty.");
            }

            // Construct the target file path
            String targetFilePath = UPLOAD_DIR + File.separator + multipartFile.getOriginalFilename();

            // Copy the file to the target directory
            Files.copy(multipartFile.getInputStream(), Paths.get(targetFilePath), StandardCopyOption.REPLACE_EXISTING);
            isUploaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isUploaded;
    }

    // Method to serve the file
    public FileSystemResource getFile(String filename) throws IOException {
        // Construct file path
        String filePath = UPLOAD_DIR + File.separator + filename;
        FileSystemResource fileResource = new FileSystemResource(filePath);

        if (!fileResource.exists()) {
            throw new IOException("File not found");
        }

        return fileResource;
    }

    public String getUploadDir() {
        return UPLOAD_DIR;
    }
}
