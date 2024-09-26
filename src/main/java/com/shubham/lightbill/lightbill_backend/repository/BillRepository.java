package com.shubham.lightbill.lightbill_backend.repository;

import com.shubham.lightbill.lightbill_backend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
}
