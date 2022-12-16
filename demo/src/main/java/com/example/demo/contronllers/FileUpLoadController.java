package com.example.demo.contronllers;

import com.example.demo.models.ResponseObject;
import com.example.demo.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/api/v1/FileUpLoad")
public class FileUpLoadController {
    @Autowired
    private IStorageService iStorageService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> upLoadFile(@RequestParam("file")MultipartFile file) {
        try {
            String generatedFileName = iStorageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Upload file successfully", generatedFileName)
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("UPLOAD FAILED", exception.getMessage(), "")
            );
        }
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = iStorageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        }catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }
    //How to load all uploaded files?
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUpLoadFiles() {
        try {
            List<String> urls = iStorageService.loadAll()
                    .map(path -> {
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUpLoadController.class,
                                "readDetailFile", path.getFileName().toString()).build().toUri().toString();
                        return urlPath;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("OK", "LIST FILES SUCCESSFULLY", urls));
        } catch (Exception exception) {
            return ResponseEntity.ok(new ResponseObject("FAILED", "LIST FILES FAILED", new String[] {}));
        }
    }
}
