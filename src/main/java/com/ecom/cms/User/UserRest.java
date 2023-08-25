package com.ecom.cms.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserRest {

    @Autowired
    UserService userService;

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap) {
        return userService.signUp(requestMap);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap) {
        return userService.login(requestMap);
    }

    @GetMapping(path = "/get")
    public List<UserDto> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping(path = "/getLogginUser")
    public UserDto getLogginUser() {
        return userService.getLogginUser();
    }

    @PatchMapping(path = "/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> requestMap) {
        return userService.updateStatus(requestMap);
    }

    @PatchMapping(path = "/updateRole")
    public ResponseEntity<String> updateRole(@RequestBody Map<String, Object> requestMap) {
        return userService.updateRole(requestMap);
    }

    @GetMapping(path = "/checkToken")
    public ResponseEntity<String> checkToken() {
        return userService.checkToken();
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap) {
        return userService.changePassword(requestMap);
    }

    @PostMapping(path = "/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap) {
        return userService.forgotPassword(requestMap);
    }

    @PostMapping(path = "/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String oneTimeToken, @RequestBody Map<String, String> requestMap) {
        return userService.resetPassword(oneTimeToken, requestMap);
    }

    @PatchMapping(path = "/updateWishlist")
    public ResponseEntity<String> updateWishlist(@RequestBody Map<String, String> requestMap) {
        return userService.updateWishlist(requestMap);
    }
}