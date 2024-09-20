package com.shubham.lightbill.lightbill_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private String role;

    @Column(nullable = false)
    private Boolean isActive;
}
