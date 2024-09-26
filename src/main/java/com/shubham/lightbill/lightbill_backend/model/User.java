package com.shubham.lightbill.lightbill_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shubham.lightbill.lightbill_backend.constants.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String phNo;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walletId", referencedColumnName = "walletId")
    @JsonBackReference
    private Wallet wallet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bill> bills;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean isBlocked;
}
