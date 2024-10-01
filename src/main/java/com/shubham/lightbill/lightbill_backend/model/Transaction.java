package com.shubham.lightbill.lightbill_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shubham.lightbill.lightbill_backend.constants.PaymentMethod;
import com.shubham.lightbill.lightbill_backend.constants.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String txnId;

    @ManyToOne()
    @JoinColumn(name = "billId", referencedColumnName = "billId", nullable = false)
    @JsonIgnoreProperties(value = {"txnList"})
    private Bill bill;

    @ManyToOne()
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;
}
