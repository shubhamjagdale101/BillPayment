package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private UserRepository userRepository;

    public User getCustomerProfile(String userId){
        return userRepository.findByUserId(userId);
    }
}
