package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.annotation.ValidEnum;
import com.shubham.lightbill.lightbill_backend.annotation.WithRateLimitProtection;
import com.shubham.lightbill.lightbill_backend.constants.OtpType;
import com.shubham.lightbill.lightbill_backend.dto.SignUpDto;
import com.shubham.lightbill.lightbill_backend.model.Otp;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.repository.OtpRepository;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authservice;

    @GetMapping("/generateOtp")
    @Transactional
    public ApiResponse<Object> sendOtpEmail(@RequestParam("userId") String userId) throws Exception {
        authservice.sendOtpToEmail(userId);
        return ApiResponse.success(null, "sending otp", HttpStatus.PROCESSING.value());
    }

    @WithRateLimitProtection(rateLimit = 20, rateDuration = 60000)
    @GetMapping("/verifyOtp")
    public ApiResponse<Integer> verifyOtp(
            @RequestParam("userId") String userId,
            @RequestParam("otpType") String otpType,
            @RequestParam("otp") String otp
    ){
        Boolean res = authservice.verifyOtp(otp, userId, OtpType.valueOf(otpType));
        return ApiResponse.success(10, Thread.currentThread().getName(), HttpStatus.OK.value());
    }

    @PostMapping("/signUpUser")
    public ApiResponse<List<Object>> signUpUser(@RequestBody SignUpDto req){
        List<Object> res = authservice.signUpUser(req);
        return ApiResponse.success(res, "", 200);
    }
}
