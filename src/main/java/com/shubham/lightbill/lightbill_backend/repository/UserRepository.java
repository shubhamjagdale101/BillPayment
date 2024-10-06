package com.shubham.lightbill.lightbill_backend.repository;

import com.shubham.lightbill.lightbill_backend.constants.Role;
import com.shubham.lightbill.lightbill_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByUserId(String userId);
    Page<User> findByRole(Role role, Pageable pageable);
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);
    Page<User> findByRoleAndIsActive(Role role, Boolean isActive, Pageable pageable);
    boolean existsByEmail(String email);
}
