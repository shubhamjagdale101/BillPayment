package com.shubham.lightbill.lightbill_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniqueID {
    @Id
    private String name;

    @Column(nullable = false)
    private Integer counter;
}
