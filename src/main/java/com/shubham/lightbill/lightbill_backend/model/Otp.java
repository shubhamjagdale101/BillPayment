package com.shubham.lightbill.lightbill_backend.model;

import com.shubham.lightbill.lightbill_backend.constants.OtpType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Otp {
    @EmbeddedId
    private OtpCompositeKey key;

    @Column(nullable = false)
    private String otp;

    @UpdateTimestamp
    private Date updatedTime;
}
