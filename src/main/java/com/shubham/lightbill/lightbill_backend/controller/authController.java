package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.model.Otp;
import com.shubham.lightbill.lightbill_backend.repository.OtpRepository;
import com.shubham.lightbill.lightbill_backend.service.AuthService;
import com.shubham.lightbill.lightbill_backend.service.ThreadPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class authController {
    @Autowired
    private AuthService authservice;

    @Autowired
    private OtpRepository otpRepository;

    @PostMapping("/otpLogin")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> payload) {
        SecureRandom random = new SecureRandom();

        String email = payload.get("emailId");
        String username = payload.get("username");
        String generatedOtp = String.format("%06d", random.nextInt(999999));

        Otp otp = otpRepository.findByUserName(username);
        if(otp != null) otpRepository.updateOtp(username, generatedOtp);
        else {
            otp = Otp.builder()
                    .userName(username)
                    .otp(generatedOtp)
                    .build();
            otpRepository.save(otp);
        }

        Runnable task = () -> {
            try{
                authservice.sendEmail(email, username, generatedOtp);
            } catch (Exception ignored) {}
        };

        ThreadPoolService threadPool = ThreadPoolService.getInstance();
        threadPool.submitTask(task);
        return new ResponseEntity<>("sending otp", HttpStatus.OK);
    }
}
