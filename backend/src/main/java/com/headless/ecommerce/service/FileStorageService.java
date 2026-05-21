package com.headless.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * File storage service interface for file upload/delete operations.
 */
public interface FileStorageService {

    /**
     * Stores a file and returns the accessible URL path.
     *
     * @param file the multipart file to store
     * @param subDir the subdirectory within the upload directory
     * @return the URL path to the stored file
     */
    String store(MultipartFile file, String subDir);

    /**
     * Deletes a file by its URL path.
     *
     * @param fileUrl the URL path of the file to delete
     */
    void delete(String fileUrl);
}
