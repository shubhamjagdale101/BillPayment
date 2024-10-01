package com.shubham.lightbill.lightbill_backend.repository;

import com.shubham.lightbill.lightbill_backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    boolean existsByEmail(String email);
}
