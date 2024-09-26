package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.constants.PaymentStatus;
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
public class BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdGeneratorService idGeneratorService;

    public Bill addBill(BillDto req){
        User user = userRepository.findByUserId(req.getUserId());
        Bill bill = Bill.builder()
            .billId(idGeneratorService.generateId(Bill.class.getName(), "Bill"))
            .amount(req.getAmount())
            .discount(req.getDiscount())
            .monthAndYear(req.getMonthAndYear())
            .dueDate(req.getDueDate())
            .unitConsumption(req.getUnitConsumption())
            .user(user)
            .paymentStatus(PaymentStatus.UNPAID)
            .build();
        return billRepository.save(bill);
    }
}
