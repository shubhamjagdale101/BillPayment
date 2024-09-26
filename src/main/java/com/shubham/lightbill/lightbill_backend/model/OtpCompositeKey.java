package com.shubham.lightbill.lightbill_backend.model;

import com.shubham.lightbill.lightbill_backend.constants.OtpType;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpCompositeKey {
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(value = EnumType.STRING)
    private OtpType type;
}
