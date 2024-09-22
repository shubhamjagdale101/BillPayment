package com.shubham.lightbill.lightbill_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String billId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Column(nullable = false)
    private Integer unitConsumption;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private Integer amount;
}
