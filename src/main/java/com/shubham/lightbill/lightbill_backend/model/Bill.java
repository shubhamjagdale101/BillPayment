package com.shubham.lightbill.lightbill_backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shubham.lightbill.lightbill_backend.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bill {
    @Id
    @Column(nullable = false, unique = true)
    private String billId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Column(nullable = false, unique = true)
    private String monthAndYear;

    @Column(nullable = false)
    private Integer unitConsumption;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private Integer amount;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> txnList;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;
}
