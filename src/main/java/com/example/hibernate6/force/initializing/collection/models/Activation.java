package com.example.hibernate6.force.initializing.collection.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Activation {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    LegalEntity legalEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    BankAccount bankAccount;

}
