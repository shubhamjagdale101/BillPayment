package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.dto.BillDto;
import com.shubham.lightbill.lightbill_backend.model.Bill;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.repository.BillRepository;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillService billService;


    public Bill addBill(BillDto req){
        return billService.addBill(req);
    }
}
