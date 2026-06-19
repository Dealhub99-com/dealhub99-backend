package com.dealhub99.backend.service;

import com.dealhub99.backend.entity.Image;
import com.dealhub99.backend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final ImageRepository imageRepository;

    /**
     * Uploads a file to the Database and returns a local download URL.
     */
    public String storeFile(MultipartFile file) {
        try {
            Image image = Image.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .build();

            image = imageRepository.save(image);

            // Return URL to access the image
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/media/")
                    .path(image.getId().toString())
                    .toUriString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in Database", e);
        }
    }
}
