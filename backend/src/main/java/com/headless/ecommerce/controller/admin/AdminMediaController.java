package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.Media;
import com.headless.ecommerce.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/media")
@RequiredArgsConstructor
public class AdminMediaController {

    private final MediaRepository mediaRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public ResponseEntity<Page<Media>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String folder) {
        if (folder != null && !folder.isEmpty()) {
            return ResponseEntity.ok(mediaRepository.findByFolderOrderByCreatedAtDesc(folder, PageRequest.of(page, size)));
        }
        return ResponseEntity.ok(mediaRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)));
    }

    @PostMapping("/upload")
    public ResponseEntity<Media> upload(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "folder", defaultValue = "default") String folder) throws IOException {
        String originalName = file.getOriginalFilename();
        String extension = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String storedName = UUID.randomUUID() + extension;

        Path uploadPath = Paths.get(UPLOAD_DIR, folder);
        Files.createDirectories(uploadPath);
        Files.copy(file.getInputStream(), uploadPath.resolve(storedName));

        Media media = Media.builder()
                .originalName(originalName)
                .storedName(storedName)
                .filePath(uploadPath.resolve(storedName).toString())
                .fileUrl("/uploads/" + folder + "/" + storedName)
                .fileType(extension.substring(1).toLowerCase())
                .mimeType(file.getContentType())
                .fileSize(file.getSize())
                .folder(folder)
                .build();

        return ResponseEntity.ok(mediaRepository.save(media));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IOException {
        Media media = mediaRepository.findById(id).orElseThrow();
        Path path = Paths.get(media.getFilePath());
        Files.deleteIfExists(path);
        mediaRepository.delete(media);
        return ResponseEntity.ok().build();
    }
}
