package com.ecom.cms.UTILS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MainUtils {

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }
}
