package com.ecom.cms.Product.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/image")
public class ImageRest {

    @Autowired
    ImageService imageService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(file);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable("id") int imageId) {
        return imageService.downloadImage(imageId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") int imageId) {
        return imageService.deleteImage(imageId);
    }
}
