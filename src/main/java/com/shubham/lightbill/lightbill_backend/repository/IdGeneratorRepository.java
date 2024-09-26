package com.shubham.lightbill.lightbill_backend.repository;

import com.shubham.lightbill.lightbill_backend.model.UniqueID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdGeneratorRepository extends JpaRepository<UniqueID, String> {
    UniqueID findByName(String name);
}
