package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.constants.Role;
import com.shubham.lightbill.lightbill_backend.dto.SignUpDto;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/auth")
public class AdminController {
    @Autowired
    private AuthService authservice;
    @PostMapping("/signUpUser")
    public ApiResponse<User> signUpUser(@Valid @RequestBody SignUpDto req){
        User user = authservice.signUpUser(req, Role.EMPLOYEE);
        return ApiResponse.success(user, "", 200);
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<String>> checkEmailExists(@RequestParam String email) {
        boolean emailExists = authservice.checkIfEmailExists(email);
        if (emailExists) {
            // If the email already exists, return an error response
            return ResponseEntity
                    .status(409) // HTTP 409 Conflict
                    .body(ApiResponse.error("Email already exists", 409));
        } else {
            // If the email is available, return a success response
            return ResponseEntity
                    .status(200) // HTTP 200 OK
                    .body(ApiResponse.success(null, "Email is available", 200));
        }
    }
}
