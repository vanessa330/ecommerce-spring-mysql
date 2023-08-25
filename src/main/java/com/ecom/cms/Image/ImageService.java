package com.ecom.cms.Image;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

    ResponseEntity<?> uploadImage(MultipartFile file);

    ResponseEntity<?> downloadImage(int imageId);

    ResponseEntity<?> deleteImage(int imageId);

}
