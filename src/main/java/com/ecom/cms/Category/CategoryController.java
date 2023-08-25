package com.ecom.cms.Category;

import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Optional;

@Controller
public class CategoryController implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            if (!requestMap.containsKey("name")) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            categoryRepository.save(getCategoryFromMap(requestMap, true));
            return MainUtils.getResponseEntity("Category added successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Category getCategoryFromMap(Map<String, Object> requestMap, boolean isNew) {
        Category category = Category.builder().name(requestMap.get("name").toString()).build();

        if (!isNew) {
            category.setId(((Number) requestMap.get("id")).intValue());
        }
        return category;
    }

    @Override
    public ResponseEntity<?> getAllCategory() {
        try {
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            if (!requestMap.containsKey("name")) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Optional<Category> optional = categoryRepository.findById(((Number) requestMap.get("id")).intValue());
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Category is does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            categoryRepository.save(getCategoryFromMap(requestMap, false));
            return MainUtils.getResponseEntity("Category updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(int categoryId) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Optional<Category> optional = categoryRepository.findById(categoryId);
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Category id does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            categoryRepository.deleteById(categoryId);

            return MainUtils.getResponseEntity("Category deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
