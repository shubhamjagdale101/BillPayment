package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.dto.EmployeeRegisterDTO;
import com.shubham.lightbill.lightbill_backend.model.Employee;
import com.shubham.lightbill.lightbill_backend.service.EmployeeService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class EmployeeRegistrationController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@RequestBody EmployeeRegisterDTO registrationDTO){

        Map<String, String> response = new HashMap<>();
        try{
            Employee employee = employeeService.registerEmployee(
                    registrationDTO.getFirstName(),
                    registrationDTO.getLastName(),
                    registrationDTO.getEmail(),
                    registrationDTO.getPhoneNumber()
            );
            response.put("message", "Registration successful. Your Employee ID is: " + employee.getEmployeeId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MessagingException e) {
            response.put("error", "Error sending email: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}
