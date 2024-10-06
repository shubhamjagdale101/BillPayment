package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.constants.Role;
import com.shubham.lightbill.lightbill_backend.dto.BillDto;
import com.shubham.lightbill.lightbill_backend.model.Bill;
import com.shubham.lightbill.lightbill_backend.model.Employee;
import com.shubham.lightbill.lightbill_backend.model.Transaction;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.repository.EmployeeRepository;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillService billService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmailService emailService;



    public Employee registerEmployee(String firstName,String lastName, String email, String phoneNumber) throws MessagingException {
        if(employeeRepository.existsByEmail(email)){
            throw new RuntimeException("Email already exists");
        }

//        employee id

        String employeeId = "EMP" + UUID.randomUUID().toString().substring(0,5).toUpperCase();

        Employee employee = Employee.builder()
                .employeeId(employeeId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        employeeRepository.save(employee);

        emailService.sendEmailToEmployee(employeeId,email);

        return employee;

    }

    public Bill addBill(BillDto req) throws Exception {
        return billService.addBill(req);
    }

    public Page<User> getUserWithCustomerRoleWithPagination(Pageable pageable){
        Page<User> page = userRepository.findByRole(Role.CUSTOMER, pageable);
        return page;
    }

    public User getEmployeeProfile(String userId){
        return userRepository.findByUserId(userId);
    }

    public Page<User> getCustomerWithActiveStatus(Pageable pageable){
        return userRepository.findByRoleAndIsActive(Role.CUSTOMER,true, pageable);
    }

    public Page<User> getCustomerWithInactiveStatus(Pageable pageable){
        return userRepository.findByRoleAndIsActive(Role.CUSTOMER,false, pageable);
    }

    public List<User> getUserWithCustomerRoleByStatus(Boolean status, Pageable pageble) throws Exception {
        Page<User> page = null;
        if(status) page = getCustomerWithActiveStatus(pageble);
        else page = getCustomerWithInactiveStatus(pageble);
        return page.getContent();
    }

    public Page<Transaction> getTransactionByFilterWithPagination(String filterBy, String filterValue, Pageable pageable) throws Exception {
        return transactionService.getTransactionByFilterWithPagination(filterBy, filterValue, pageable);
    }
}
