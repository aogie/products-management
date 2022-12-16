package com.example.demo.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService{

    private final Path storageFolder = Paths.get("upload");

    //Contructor
    public ImageStorageService() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException exception) {
            throw new RuntimeException("Cannot initialize storge", exception);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp", "jfif"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to storage emty file.");
            }
            //Check file is image?
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            //File must be <= 5MB
            float fileSizeInMegabytes = file.getSize() / 1000000.0f;
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5MB");
            }
            //File must be renamed
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                    Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("CANNOT STORAGE FILE OUTSIDE CURRENT DIRECTORY.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;

        } catch (IOException exception) {
            throw new RuntimeException("FAILED TO STORAGE EMTY FILE.", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        } catch (IOException exception) {
            throw new RuntimeException("FAILED TO LOAD STORED FILES", exception);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
