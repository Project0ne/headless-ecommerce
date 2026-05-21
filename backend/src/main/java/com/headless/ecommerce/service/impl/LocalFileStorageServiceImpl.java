package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Local file system implementation of FileStorageService.
 */
@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final String uploadDir;

    public LocalFileStorageServiceImpl(@Value("${file.upload-dir:uploads/}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public String store(MultipartFile file, String subDir) {
        if (file.isEmpty()) {
            throw new BusinessException("Cannot store empty file");
        }

        try {
            // Create directory if not exists
            Path dirPath = Paths.get(uploadDir, subDir).toAbsolutePath().normalize();
            Files.createDirectories(dirPath);

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // Save file
            Path filePath = dirPath.resolve(filename);
            file.transferTo(filePath.toFile());

            return "/uploads/" + subDir + "/" + filename;
        } catch (IOException e) {
            throw new BusinessException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/uploads/")) {
            return;
        }

        String relativePath = fileUrl.substring("/uploads/".length());
        Path filePath = Paths.get(uploadDir, relativePath).toAbsolutePath().normalize();

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new BusinessException("Failed to delete file: " + e.getMessage());
        }
    }
}
