package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.dto.BillDto;
import com.shubham.lightbill.lightbill_backend.model.Bill;
import com.shubham.lightbill.lightbill_backend.model.Transaction;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.BillService;
import com.shubham.lightbill.lightbill_backend.service.EmployeeService;
import com.shubham.lightbill.lightbill_backend.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BillService billService;

    @PostMapping("/addBill")
    public ApiResponse<Bill> addBill(@Valid @RequestBody BillDto req) throws Exception {
        Bill bill = employeeService.addBill(req);
        return ApiResponse.success(bill, "Bill get Added Successfully", 200);
    }

    @GetMapping("/getTransactions")
    public ApiResponse<List<Transaction>> getTransactions(
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Transaction> res = transactionService.getTransactionUsingPagination(pageable);
        return ApiResponse.success(res, "successfully fetched transactions.", HttpStatus.OK.value());
    }

    @GetMapping("/getTransactionsByFilter")
    public ApiResponse<Object> getTransactionsByFilter(
            @RequestParam(name = "filterBy") String filterBy,
            @RequestParam(name = "filterValue") String filterValue,
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Transaction> result = employeeService.getTransactionByFilterWithPagination(filterBy, filterValue, pageable);
        return ApiResponse.success(result.getContent(), "", HttpStatus.OK.value());
    }

    @PutMapping("/markCashPayment/{txnId}")
    public ApiResponse<Transaction> markPaymentAsCash(@PathVariable String txnId) throws Exception {
        Transaction transaction = transactionService.markPaymentAsCash(txnId);
        return ApiResponse.success(transaction, "Payment marked as cash", HttpStatus.OK.value());
    }

    @GetMapping("/getBills")
    public ApiResponse<List<Bill>> getBills(
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Bill> result = billService.getBillsUsingPagination(pageable);
        return ApiResponse.success(result, "", HttpStatus.OK.value());
    }

    @GetMapping("/getBillById/{billId}")
    public ApiResponse<Bill> getBillById(@PathVariable String billId) throws Exception {
        Bill bill = billService.findBillById(billId);
        if (bill == null) {
            throw new Exception("Bill not found");
        }
        return ApiResponse.success(bill, "Bill fetched successfully", HttpStatus.OK.value());
    }

    @GetMapping("/getCustomers")
    public ApiResponse<List<User>> getCustomers(
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> result = employeeService.getUserWithCustomerRoleWithPagination(pageable);
        return ApiResponse.success(result.getContent(),  ((Integer) result.getTotalPages()).toString(), HttpStatus.OK.value());
    }

    @GetMapping("/getCustomersByFilter")
    public ApiResponse<List<User>> getCustomersByFilter(
            @RequestParam(name = "isActiveStatus", defaultValue = "true") Boolean isActiveStatus,
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ) throws Exception {
        List<Object> res = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<User> result = employeeService.getUserWithCustomerRoleByStatus(isActiveStatus, pageable);
        return ApiResponse.success(result, "", HttpStatus.OK.value());
    }

    @GetMapping("/getEmployeeProfile")
    public ApiResponse<User> getEmployeeProfile(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User employeeProfile = employeeService.getEmployeeProfile(userId);
        return ApiResponse.success(employeeProfile, "", 100);
    }
}
