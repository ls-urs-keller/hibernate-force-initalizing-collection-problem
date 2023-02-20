package com.example.hibernate6.force.initializing.collection.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LegalEntity {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne(mappedBy = "legalEntity", fetch = FetchType.EAGER)
    Ownership ownership;

    @OneToMany(mappedBy = "legalEntity", fetch = FetchType.EAGER)
    Set<BankAccount> bankAccounts = new HashSet<>();

}
