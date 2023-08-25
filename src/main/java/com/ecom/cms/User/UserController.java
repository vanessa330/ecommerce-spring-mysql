package com.ecom.cms.User;

import com.ecom.cms.JWT.MyUserDetailsService;
import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.JWT.JwtUtil;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import com.ecom.cms.UTILS.EmailUtils;
import org.modelmapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class UserController implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
//        log.info("Inside signUp{}", requestMap);
        try {
            if (!validateSignUpMap(requestMap)) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            User newUser = userRepository.findByEmail(requestMap.get("email"));

            if (!Objects.isNull(newUser)) {
                return MainUtils.getResponseEntity("Email Already Exits.", HttpStatus.NOT_ACCEPTABLE);
            }

            userRepository.save(createNewUser(requestMap));

            return MainUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("email")
                && requestMap.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    //   JSON requestMap
    //   {
    //    "name": "User Name",
    //    "email": "xxx@mailinator.com",
    //    "password": "123456"
    //    }
    private User createNewUser(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("active");
        user.setRole("user");
        user.setOneTimeToken(null);
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (myUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("active")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(myUserDetailsService.getUserDetail().getEmail(),
                                    myUserDetailsService.getUserDetail().getRole())
                            + "\", \"name\": \"" + myUserDetailsService.getUserDetail().getName() + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"Your account has been suspended.\"}",
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<String>("{\"message\":\"Bad Credentials.\"}",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>("{\"message\":\"An error occurred.\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<UserDto> getAllUser() {
        if (!jwtFilter.isAdmin()) {
            return null;
        }

        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        return userDtoList;
    }

    @Override
    public UserDto getLogginUser() {
        Optional<User> currentUser = Optional.ofNullable(userRepository.findByEmail(jwtFilter.getCurrentUser()));

        if (currentUser.isPresent()) {
            User user = currentUser.get();
            UserDto userDto = modelMapper.map(user, UserDto.class);
            return userDto;
        }
        return null;
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Optional<User> optional = userRepository.findById(((Number) requestMap.get("id")).intValue());

            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("User id doesn't exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            userRepository.updateStatus(requestMap.get("status").toString(), ((Number) requestMap.get("id")).intValue());

            return MainUtils.getResponseEntity("User status updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateRole(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Optional<User> optional = userRepository.findById(((Number) requestMap.get("id")).intValue());

            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("User id doesn't exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            userRepository.updateRole(requestMap.get("role").toString(), ((Number) requestMap.get("id")).intValue());
            sendRoleMailToAdmin(requestMap.get("role").toString(), optional.get().getEmail(), userRepository.getAllAdminEmail());

            return MainUtils.getResponseEntity("User role updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendRoleMailToAdmin(String role, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());

        if (role.equalsIgnoreCase("admin")) {
            emailUtils.sendNotification(jwtFilter.getCurrentUser(), "Admin Role Approved",
                    "USER: " + user + "\n was given admin privileges by \nADMIN: " + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendNotification(jwtFilter.getCurrentUser(), "Admin Role Disabled",
                    "USER: " + user + "\n admin privileges were revoked by \nADMIN: " + jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return MainUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());

            if (!passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
                return MainUtils.getResponseEntity("Incorrect old password.", HttpStatus.BAD_REQUEST);
            }

            userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
            userRepository.save(userObj);

            return MainUtils.getResponseEntity("Password updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(requestMap.get("email"));

            if (userObj == null) {
                return MainUtils.getResponseEntity("Incorrect email address.", HttpStatus.NOT_ACCEPTABLE);
            }

            String oneTimeToken = UUID.randomUUID().toString();
            userObj.setOneTimeToken(oneTimeToken);
            userRepository.save(userObj);

            emailUtils.sendResetPassword(userObj.getEmail(), "Reset Your Password", oneTimeToken);

            // Schedule task to delete one-time token after 15 minutes
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                userObj.setOneTimeToken(null);
                userRepository.save(userObj);
            }, 15, TimeUnit.MINUTES);

            return MainUtils.getResponseEntity("Check your mail for reset your password.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> resetPassword(String oneTimeToken, Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(requestMap.get("email"));

            if (userObj == null) {
                return MainUtils.getResponseEntity("Incorrect email address.", HttpStatus.NOT_ACCEPTABLE);
            }

            if (userObj.getOneTimeToken() == null) {
                return MainUtils.getResponseEntity("Expired one time token", HttpStatus.BAD_REQUEST);
            }

            if (!userObj.getOneTimeToken().equals(oneTimeToken)) {
//                if (!userObj.getOneTimeToken().equals(requestMap.get("oneTimeToken"))) {
                return MainUtils.getResponseEntity("Invalid one time token.", HttpStatus.BAD_REQUEST);
            }

            userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
            userObj.setOneTimeToken(null);
            userRepository.save(userObj);

            return MainUtils.getResponseEntity("Password reset successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateWishlist(Map<String, String> requestMap) {
        try {
            Optional<User> currentUser = Optional.ofNullable(userRepository.findByEmail(jwtFilter.getCurrentUser()));

            if (!currentUser.isPresent()) {
                return MainUtils.getResponseEntity("User doesn't exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            User user = currentUser.get();
            userRepository.updateWishlist(requestMap.get("wishlist"), user.getEmail());

            return MainUtils.getResponseEntity("User wishlist updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
