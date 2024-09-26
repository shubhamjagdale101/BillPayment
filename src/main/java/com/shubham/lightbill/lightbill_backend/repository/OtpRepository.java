package com.shubham.lightbill.lightbill_backend.repository;

import com.shubham.lightbill.lightbill_backend.model.Otp;
import com.shubham.lightbill.lightbill_backend.model.OtpCompositeKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, OtpCompositeKey> {
    Otp findByKey(OtpCompositeKey key);

    @Modifying
    @Query("UPDATE Otp o SET o.otp = :newOtp WHERE o.key = :key")
    void updateOtp(OtpCompositeKey key, String newOtp);
}
