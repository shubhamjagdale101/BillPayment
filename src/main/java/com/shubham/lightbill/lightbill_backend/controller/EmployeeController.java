package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.dto.BillDto;
import com.shubham.lightbill.lightbill_backend.dto.FilterDto;
import com.shubham.lightbill.lightbill_backend.model.Bill;
import com.shubham.lightbill.lightbill_backend.repository.TransactionRepository;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/addBill")
    public ApiResponse<Bill> addBill(@Valid @RequestBody BillDto req){
        Bill bill = employeeService.addBill(req);
        return ApiResponse.success(bill, "Bill get Added Successfully", 200);
    }

    @PostMapping("/getTransactions")
    public ApiResponse<Object> getTransactions(@Valid @RequestBody FilterDto filter){
        List<Object> res = new ArrayList<>();
        return ApiResponse.success(res, "", HttpStatus.OK.value());
    }

    @PostMapping("/getBills")
    public ApiResponse<Object> getBills(@Valid @RequestBody FilterDto filterDto){
        return ApiResponse.success("", "", HttpStatus.OK.value());
    }

    @PostMapping("/getUser")
    public ApiResponse<Object> getUsers(@Valid @RequestBody FilterDto filter){
        List<Object> res = new ArrayList<>();
        return ApiResponse.success(res, "", HttpStatus.OK.value());
    }
}
