package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Page<Media> findByFolderOrderByCreatedAtDesc(String folder, Pageable pageable);
    Page<Media> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Media> findByFileTypeOrderByCreatedAtDesc(String fileType);
}
