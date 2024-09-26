package com.shubham.lightbill.lightbill_backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shubham.lightbill.lightbill_backend.constants.TransactionStatus;
import com.shubham.lightbill.lightbill_backend.constants.WalletStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wallet {
    @Id
    @Column(nullable = false, unique = true)
    private String walletId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false, unique = true)
    @JsonManagedReference
    private User user;

    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    @Column(nullable = false)
    private Integer balance;
}
