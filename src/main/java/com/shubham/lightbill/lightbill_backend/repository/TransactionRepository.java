package com.shubham.lightbill.lightbill_backend.repository;

import com.shubham.lightbill.lightbill_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
