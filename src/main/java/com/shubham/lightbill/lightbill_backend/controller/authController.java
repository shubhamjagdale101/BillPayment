package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class authController {
    @Autowired
    private AuthService authservice;

    @PostMapping("/")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> payload) {
        SecureRandom random = new SecureRandom();

        String email = payload.get("emailId");
        String username = payload.get("username");
        String otp = String.format("%06d", random.nextInt(999999));

        try{
            authservice.sendEmail(email, username, otp);
            return new ResponseEntity<>("otp send successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Arrays.toString(e.getStackTrace()), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
