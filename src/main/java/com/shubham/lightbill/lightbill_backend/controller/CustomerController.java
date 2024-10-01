package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.model.Bill;
import com.shubham.lightbill.lightbill_backend.model.Transaction;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.BillService;
import com.shubham.lightbill.lightbill_backend.service.CustomerService;
import com.shubham.lightbill.lightbill_backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BillService billService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/getEmployeeProfile")
    public ApiResponse<User> getEmployeeProfile(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User customerProfile = customerService.getCustomerProfile(userId);
        return ApiResponse.success(customerProfile, "", 100);
    }

    @GetMapping("/getBills")
    public ApiResponse<List<Bill>> getBills(
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ){
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Bill> result = billService.getBillsUsingPagination(pageable);
        return ApiResponse.success(result, "", HttpStatus.OK.value());
    }

    @GetMapping("/getTransactions")
    public ApiResponse<List<Transaction>> getTransactions(
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ){
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Transaction> res = transactionService.getTransactionUsingPaginationForUserId(userId, pageable);
        return ApiResponse.success(res, "successfully fetched transactions.", HttpStatus.OK.value());
    }
}
