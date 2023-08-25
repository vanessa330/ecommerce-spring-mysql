package com.ecom.cms.Image;

import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import com.ecom.cms.UTILS.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class ImageController implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<?> uploadImage(MultipartFile file) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Image savedImage = imageRepository.save(Image.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes())).build());

            return new ResponseEntity<>(savedImage.getId(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> downloadImage(int imageId) {
        try {
            Optional<Image> optional = imageRepository.findById(imageId);

            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Image does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            byte[] image = ImageUtils.decompressImage(optional.get().getImageData());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> deleteImage(int imageId) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Optional<Image> optional = imageRepository.findById(imageId);

            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Image does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            imageRepository.deleteById(imageId);
            return new ResponseEntity<>(imageId, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
