package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.constants.FilterTypes;
import com.shubham.lightbill.lightbill_backend.constants.PaymentMethod;
import com.shubham.lightbill.lightbill_backend.constants.TransactionStatus;
import com.shubham.lightbill.lightbill_backend.model.Transaction;
import com.shubham.lightbill.lightbill_backend.repository.TransactionRepository;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getTransactionUsingPagination(Pageable pageable){
        Page<Transaction> page = transactionRepository.findAll(pageable);
        return page.getContent();
    }

    public List<Transaction> getTransactionUsingPaginationForUserId(String userid, Pageable pageable){
        Page<Transaction> page = transactionRepository.findAll(pageable);
        return page.getContent();
    }

    private Page<Transaction> getTransactionByFilterTransactionStatus(String filterValue, Pageable pageable) throws Exception {
        try{
            TransactionStatus status = TransactionStatus.valueOf(filterValue);
            return transactionRepository.findByTransactionStatus(status, pageable);
        } catch(IllegalStateException ex){
            throw new Exception("there is no transaction status like" + filterValue);
        }
    }

    private Page<Transaction> getTransactionByFilterPaymentMethod(String filterValue, Pageable pageable) throws Exception {
        try{
            PaymentMethod method = PaymentMethod.valueOf(filterValue);
            return transactionRepository.findByPaymentMethod(method, pageable);
        } catch(IllegalStateException ex){
            throw new Exception("there is no payment method like" + filterValue);
        }
    }

    public Page<Transaction> getTransactionByFilterWithPagination(String filterBy, String filterValue, Pageable pageable) throws Exception {
        if(filterBy.equals(FilterTypes.FILTER_TYPE_FOR_TRANSACTION_BY_TRANSACTION_STATUS)){
            return getTransactionByFilterTransactionStatus(filterValue, pageable);
        } else if(filterBy.equals(FilterTypes.FILTER_TYPE_FOR_TRANSACTION_BY_PAYMENT_METHOD)){
            return getTransactionByFilterPaymentMethod(filterValue, pageable);
        } else {
            throw new Exception("filter " + filterBy + " not supported");
        }
    }
}
