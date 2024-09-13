package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor(force = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type")
    private String accountType;

    private double balance;

    @ManyToOne
    private User user;

}
