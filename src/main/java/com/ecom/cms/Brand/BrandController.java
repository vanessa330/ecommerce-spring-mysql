package com.ecom.cms.Brand;

import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Optional;

@Controller
public class BrandController implements BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewBrand(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            if (!requestMap.containsKey("name")) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            brandRepository.save(getBrandFromMap(requestMap, true));
            return MainUtils.getResponseEntity("Brand added successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Brand getBrandFromMap(Map<String, Object> requestMap, boolean isNew) {
        Brand brand = Brand.builder().name(requestMap.get("name").toString()).build();

        if (!isNew) {
            brand.setId(((Number) requestMap.get("id")).intValue());
        }
        return brand;
    }

    @Override
    public ResponseEntity<?> getAllBrand() {
        try {
            return new ResponseEntity<>(brandRepository.findAll(Sort.by(Sort.Direction.ASC, "name")), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<String> updateBrand(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            if (!requestMap.containsKey("name")) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Optional<Brand> optional = brandRepository.findById(((Number) requestMap.get("id")).intValue());
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Brand is does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            brandRepository.save(getBrandFromMap(requestMap, false));
            return MainUtils.getResponseEntity("Brand updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteBrand(int brandId) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Optional<Brand> optional = brandRepository.findById(brandId);
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Brand id does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            brandRepository.deleteById(brandId);

            return MainUtils.getResponseEntity("Brand deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
