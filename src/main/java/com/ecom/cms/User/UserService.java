package com.ecom.cms.User;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    List<UserDto> getAllUser();

    UserDto getLogginUser();

    ResponseEntity<String> updateStatus(Map<String, Object> requestMap);

    ResponseEntity<String> updateRole(Map<String, Object> requestMap);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    ResponseEntity<String> forgotPassword(Map<String, String> requestMap);

    ResponseEntity<String> resetPassword(String oneTimeToken, Map<String, String> requestMap);

    ResponseEntity<String> updateWishlist(Map<String, String> requestMap);
}
