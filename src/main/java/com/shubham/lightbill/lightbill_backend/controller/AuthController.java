package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.annotation.ValidEnum;
import com.shubham.lightbill.lightbill_backend.annotation.WithRateLimitProtection;
import com.shubham.lightbill.lightbill_backend.configuration.JwtUtil;
import com.shubham.lightbill.lightbill_backend.constants.OtpType;
import com.shubham.lightbill.lightbill_backend.constants.Role;
import com.shubham.lightbill.lightbill_backend.dto.SignUpDto;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authservice;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @GetMapping("/generateOtp")
    @Transactional
    public ApiResponse<Object> sendOtpEmail(@RequestParam("userId") String userId) throws Exception {
        authservice.sendOtpToEmail(userId);
        return ApiResponse.success(null, "sending otp", HttpStatus.PROCESSING.value());
    }

    @WithRateLimitProtection(rateLimit = 20, rateDuration = 60000)
    @GetMapping("/verifyOtp")
    public ApiResponse<String> verifyOtp(
            @RequestParam("userId") String userId,
            @RequestParam("otpType") @ValidEnum(enumClass = OtpType.class) String otpType,
            @RequestParam("otp") String otp,
            HttpServletResponse response
    ){
        Boolean res = authservice.verifyOtp(otp, userId, OtpType.valueOf(otpType));
        if(!res) return ApiResponse.error("incorrect otp", 100);

        User user = userRepository.findByUserId(userId);
        String token = jwtUtil.generateToken(userId, String.valueOf(user.getRole()));
        Cookie cookie = authservice.generateCookie("Bearer-token", token.trim());
        response.addCookie(cookie);
        return ApiResponse.success("login successful", URLEncoder.encode(token), HttpStatus.OK.value());
    }

    @PostMapping("/signUpUser")
    public ApiResponse<User> signUpUser(@Valid @RequestBody SignUpDto req){
        User user = authservice.signUpUser(req, Role.CUSTOMER);
        return ApiResponse.success(user, "", 200);
    }
}
